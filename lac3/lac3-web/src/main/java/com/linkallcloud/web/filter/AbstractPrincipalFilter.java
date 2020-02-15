package com.linkallcloud.web.filter;

import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.AccountMapping;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


public abstract class AbstractPrincipalFilter extends LacCommonFilter {

    public AbstractPrincipalFilter() {
        super();
    }

    public AbstractPrincipalFilter(List<String> ignoreRes, boolean override) {
        super(ignoreRes, override);
    }

    @Override
    protected void doConcreteFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionUser u = getLoginUser(getAppCode(), request);
        if (null == u) { // suser为空，表示初次登陆或者本地session超时
            // 若有sso, 并且sso未超时, principal为sso验证后的用户帐号
            Principal principal = getSSOPrincipal(getAppCode(), request);
            if (principal != null) {// SSO认证，且已经通过SSO认证，但是本地未登陆
                loginUser(request, getAppCode(), getSessionUserByPrincipal(principal));
                chain.doFilter(request, response);
                return;
            }

            //若有token
            String token = getLacToken(request);
            if (!Strings.isBlank(token)) {
                loginUser(request, getAppCode(), getSessionUserByToken(token));
                chain.doFilter(request, response);
                return;
            }

            //无有效登录凭证
            if (isIndexOrLocalLoginRequest(request)) {// 访问首页(登陆页面)
                chain.doFilter(request, response);
                return;
            } else {
                toLogin(getLoginUrl(), request, response);
                return;
            }
        } else {
            String currentAppCode = Controllers.getCurrentAppKey();
            String thisAppCode = getAppCode();
            if (Strings.isBlank(currentAppCode) || !currentAppCode.equals(thisAppCode)) {
                Controllers.switchLogin2App(thisAppCode);
            }
        }

        chain.doFilter(request, response);
    }


    /**
     * 查询是否传递了token
     *
     * @param request
     * @return
     */
    private String getLacToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        log.info("################  paream token:" + token);

        if (Strings.isBlank(token)) {
            token = request.getHeader("token");
            try {
                if (!Strings.isBlank(token)) {
                    token = URLDecoder.decode(token, "UTF8");
                }
            } catch (UnsupportedEncodingException e1) {
            }
            log.info("################ header token:" + token);
        }
        return token;
    }


    protected abstract String getAppCode();

    protected abstract String getLoginUrl();

    /**
     * 是否系统首页或者是本地登录的请求，用于非SSO认证过滤时的判断
     *
     * @param request
     * @return boolean
     */
    protected boolean isIndexOrLocalLoginRequest(HttpServletRequest request) {
        String sp = request.getServletPath();
        if (sp.indexOf("/index.html") != -1 || sp.indexOf("/login") != -1) {
            return true;
        }
        return false;
    }

    /**
     * 根据token加载用户SessionUser
     *
     * @param token
     * @return
     * @throws BizException
     */
    protected SessionUser getSessionUserByToken(String token) throws BizException {
        if (!Strings.isBlank(token)) {
            try {
                SessionUser user = Controllers.checkToken(token);
                if (user != null) {
                    return user;
                }
            } catch (Throwable e) {
            }
        }
        throw new BizException("e.sso.login", "根据token加载用户SessionUser发生错误：" + token);
    }

    /**
     * 根据ssoPrincipal加载用户Dto，并转化成SessionUser
     *
     * @param ssoPrincipal
     * @return SessionUser
     */
    protected SessionUser getSessionUserByPrincipal(Principal ssoPrincipal) throws BizException {
        if (ssoPrincipal != null) {
            String loginName = getLoginNameByPrincipal(ssoPrincipal);
            SessionUser user = getUserByLoginName(loginName);
            if (user != null) {
                return user;
            }
        }
        throw new BizException("e.sso.login", "根据ssoPrincipal加载用户Dto，并转化成SessionUser发生错误");
    }

    /**
     * 根据loginName，得到登录用户
     *
     * @param loginName
     * @return
     */
    protected abstract SessionUser getUserByLoginName(String loginName);

    /**
     * 根据ssoPrincipal得到用户site的loginName
     *
     * @param ssoPrincipal
     * @return
     */
    protected String getLoginNameByPrincipal(Principal ssoPrincipal) {
        if (ssoPrincipal.getMappingType() == AccountMapping.Mapping.getCode().intValue()
                && !Strings.isBlank(ssoPrincipal.getSiteId())) {
            return ssoPrincipal.getSiteId();
        } else {
            return ssoPrincipal.getId();
        }
    }

    /**
     * 把用户信息放入session(cookie)中，可以覆盖此方法，在session(cookie)中放入多需要的信息，比如权限
     *
     * @param request
     * @param appCode
     * @param user
     */
    protected void loginUser(HttpServletRequest request, String appCode, SessionUser user) {
        Controllers.login(appCode, user);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

}
