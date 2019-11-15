package com.linkallcloud.web.interceptors;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.enums.Logical;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.web.perm.RequirePermissions;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class PermissionInterceptor extends HandlerInterceptorAdapter {
    private static Log log = Logs.get();

    protected String login;
    protected String noPermission;

    protected List<String> notFilter = Lang.list("/js/", "/css/", "/images/", "/img/", ".jpg", ".png", ".jpeg", ".js",
            ".css", "/static/", "/login", "/verifyCode", "/exit", "/unsupport", "/error", "/pub/", "/face/");

    public PermissionInterceptor() {
        super();
        this.login = "/login";
        this.noPermission = "/pub/noPermission";
    }

    public PermissionInterceptor(String login, String noPermission) {
        super();
        this.login = Strings.isBlank(login) ? "/login" : login;
        this.noPermission = Strings.isBlank(noPermission) ? "/pub/noPermission" : noPermission;
    }

    /**
     * @param ignoreRes
     * @param override  是否覆盖
     */
    public PermissionInterceptor(List<String> ignoreRes, boolean override) {
        super();
        if (ignoreRes != null && ignoreRes.size() > 0) {
            if (override) {
                this.notFilter = ignoreRes;
            } else {
                for (String res : ignoreRes) {
                    boolean exist = false;
                    for (String uri : notFilter) {
                        if (uri.equals(res)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        this.notFilter.add(res);
                    }
                }
            }
        }
        this.login = "/login";
        this.noPermission = "/pub/noPermission";
    }

    public PermissionInterceptor(List<String> ignoreRes, boolean override, String login, String noPermission) {
        this(ignoreRes, override);
        this.login = Strings.isBlank(login) ? "/login" : login;
        this.noPermission = Strings.isBlank(noPermission) ? "/pub/noPermission" : noPermission;
    }

    protected abstract String getAppCode();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        if (needFiltered(uri)) {
            // 从session中获取登录者实体
            SessionUser suser = (SessionUser) Controllers.getSessionUser(getAppCode(), request);
            if (null == suser) {
                boolean isAjaxRequest = WebUtils.isAjax(request);
                if (isAjaxRequest) {
                    // response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
                    sendAjaxLoginTimeoutResponse(request, response);
                    return false;
                }
                response.sendRedirect(getLoginUrl(request));// + getUserTypePv(request)
                return false;
            } else {
                String currentAppCode = Controllers.getCurrentAppKey();
                String thisAppCode = getAppCode();
                if (Strings.isBlank(currentAppCode) || !currentAppCode.equals(thisAppCode)) {
                    Controllers.switchLogin2App(thisAppCode);
                }
                // 如果session中存在登录者实体，则继续
                boolean canvisit = doCheckPermission(suser, request, response, handler);
                if (!canvisit) {
                    log.warn("****** 用户:" + suser.getLoginName() + ",无权执行此操作:" + request.getRequestURL());
                }
                return canvisit;
            }
        }
        return true;
    }

    /**
     * ajax返回登录超时错误信息
     *
     * @param request
     * @param response
     */
    protected void sendAjaxLoginTimeoutResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        Result<Object> result = Exceptions.makeErrorResult(Exceptions.CODE_ERROR_SESSION_TIMEOUT, "会话超时");
        result.setData(getLoginUrl(request));
        // response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
        WebUtils.out(response, result);
    }

    /**
     * @param suser
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    protected boolean doCheckPermission(SessionUser suser, HttpServletRequest request, HttpServletResponse response,
                                        Object handler) throws Exception {
        if (suser.getLoginName().equals("superadmin")) {
            return true;
        }

        RequirePermissions rps = getRequirePermissions(handler);
        if (rps != null) {
            return checkAnnotationModePermission(suser, request, response, rps);
        } else {
            return checkConfigModePermission(suser, request, response);
        }
    }

    /**
     * 系统配置模式权限过滤
     *
     * @param suser
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    protected boolean checkConfigModePermission(SessionUser suser, HttpServletRequest request,
                                                HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        uri = fitRui(uri);
        Map<String, String[]> allUriRescodeMap = loadAppUriRescodeMap(null, Long.parseLong(suser.getAppId()));
        if (allUriRescodeMap != null && allUriRescodeMap.containsKey(uri)) {
            String[] rescodes = allUriRescodeMap.get(uri);
            if (rescodes == null || rescodes.length <= 0) {
                return true;
            } else {
                for (String rescode : rescodes) {
                    if (suser.hasMenuPermission(rescode)) {
                        return true;
                    }
                }
                sendNoPermisson(request, response);
                return false;
            }
        } else {
            return true;
        }
    }

    protected String fitRui(String uri) {
        if (uri.startsWith("//")) {
            uri = uri.substring(1);
            return fitRui(uri);
        }
        return uri;
    }

    protected Map<String, String[]> loadAppUriRescodeMap(Trace trace, Long appId) {
        return null;
    }

    /**
     * 注解模式权限过滤
     *
     * @param suser
     * @param request
     * @param response
     * @param rps
     * @return
     * @throws IOException
     */
    protected boolean checkAnnotationModePermission(SessionUser suser, HttpServletRequest request,
                                                    HttpServletResponse response, RequirePermissions rps) throws IOException {
        String[] resCodes = rps.value();
        if (resCodes != null && resCodes.length > 0) {
            if (resCodes.length == 1) {
                if (!suser.hasMenuPermission(resCodes[0])) {
                    sendNoPermisson(request, response);
                    return false;
                } else {
                    return true;
                }
            } else {
                Logical logi = rps.logical();
                if (logi.equals(Logical.AND)) {
                    for (String resCode : resCodes) {
                        if (!suser.hasMenuPermission(resCode)) {
                            sendNoPermisson(request, response);
                            return false;
                        }
                    }
                    return true;
                } else {
                    for (String resCode : resCodes) {
                        if (suser.hasMenuPermission(resCode)) {
                            return true;
                        }
                    }
                    sendNoPermisson(request, response);
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    protected void sendNoPermisson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (WebUtils.isAjax(request)) {
            sendAjaxNoPermisson(request, response);
        } else {
            response.sendRedirect(getNoPermissionUrl(request));
        }
    }

    protected void sendAjaxNoPermisson(HttpServletRequest request, HttpServletResponse response) {
        Result<Object> result = Exceptions.makeErrorResult(Exceptions.CODE_ERROR_AUTH_PERMISSION, "您没有执行此操作的权限！");
        result.setData(getNoPermissionUrl(request));
        response.setCharacterEncoding("UTF-8");
        WebUtils.out(response, result);
    }

    // protected Trace getTrace(Object handler) {
    // if (handler instanceof HandlerMethod) {
    // HandlerMethod hm = (HandlerMethod) handler;
    // MethodParameter[] parameters = hm.getMethodParameters();
    // for (int i = 0; i < parameters.length; i++) {
    // MethodParameter parameter = parameters[i];
    //
    // }
    // }
    // return null;
    // }

    /**
     * @param handler
     * @return
     */
    protected RequirePermissions getRequirePermissions(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            if (hm.getBeanType().getAnnotation(Controller.class) != null) {
                Method method = hm.getMethod();
                RequirePermissions rps = method.getAnnotation(RequirePermissions.class);
                if (rps == null) {
                    rps = hm.getBeanType().getAnnotation(RequirePermissions.class);
                }
                return rps;
            }
        }
        return null;
    }

    /**
     * 是否需要过滤
     *
     * @param uri
     * @return
     */
    protected boolean needFiltered(String uri) {
        for (String s : notFilter) {
            if (uri.indexOf(s) != -1) {
                return false;
            }
        }
        return true;
    }

    protected String getLoginUrl(HttpServletRequest request) {
        if (Strings.isBlank(this.login)) {
            this.login = "/login";
        }
        if (this.login.startsWith("http://") || this.login.startsWith("https://")) {
            return this.login;
        } else {
            return request.getContextPath() + this.login;
        }
    }

    protected String getNoPermissionUrl(HttpServletRequest request) {
        if (Strings.isBlank(this.noPermission)) {
            this.noPermission = "/pub/noPermission";
        }
        if (this.noPermission.startsWith("http://") || this.noPermission.startsWith("https://")) {
            return this.noPermission;
        } else {
            return request.getContextPath() + this.noPermission;
        }
    }

}
