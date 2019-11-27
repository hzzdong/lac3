package com.linkallcloud.web.interceptors;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.enums.Logical;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
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

public abstract class AbstractPermissionInterceptor extends HandlerInterceptorAdapter {
    private static Log log = Logs.get();

    protected List<String> notFilterResources = Lang.list("/static/", "/js/", "/css/", "/images/", "/img/", ".jpg", ".png",
            ".jpeg", ".js", ".css", "/imageValidate", "/verifyCode", "/exit", "/nnl/", "/unsupport", "/error", "/pub/");

    public AbstractPermissionInterceptor() {
        super();
    }

    /**
     * @param ignoreRes
     * @param override  是否覆盖
     */
    public AbstractPermissionInterceptor(List<String> ignoreRes, boolean override) {
        super();
        if (ignoreRes != null && ignoreRes.size() > 0) {
            if (override) {
                this.notFilterResources = ignoreRes;
            } else {
                for (String res : ignoreRes) {
                    boolean exist = false;
                    for (String uri : notFilterResources) {
                        if (uri.equals(res)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        this.notFilterResources.add(res);
                    }
                }
            }
        }
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
                throw new BizException("PermissionFilter", "您没有权限访问此资源");
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
                throw new BizException("PermissionFilter", "您没有权限访问此资源");
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
                    throw new BizException("PermissionFilter", "您没有权限访问此资源");
                } else {
                    return true;
                }
            } else {
                Logical logi = rps.logical();
                if (logi.equals(Logical.AND)) {
                    for (String resCode : resCodes) {
                        if (!suser.hasMenuPermission(resCode)) {
                            throw new BizException("PermissionFilter", "您没有权限访问此资源");
                        }
                    }
                    return true;
                } else {
                    for (String resCode : resCodes) {
                        if (suser.hasMenuPermission(resCode)) {
                            return true;
                        }
                    }
                    throw new BizException("PermissionFilter", "您没有权限访问此资源");
                }
            }
        } else {
            return true;
        }
    }

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
        for (String s : notFilterResources) {
            if (uri.indexOf(s) != -1) {
                return false;
            }
        }
        return true;
    }

}
