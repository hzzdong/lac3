package com.linkallcloud.web.filter;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class CommonLoginFilter extends LacCommonFilter {

    public CommonLoginFilter() {
        super();
    }

    public CommonLoginFilter(List<String> ignoreRes, boolean override) {
        super(ignoreRes, override);
    }

    protected abstract String getAppCode();

    protected abstract String getLoginUrl();

    @Override
    protected void doConcreteFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionUser u = getLoginUser(getAppCode(), request);
        if (null == u) { // suser为空，表示初次登陆或者本地session超时
            if (isIndexOrLocalLoginRequest(request)) {// 不是SSO认证，且是访问首页(登陆页面)
                chain.doFilter(request, response);
                return;
            } else {
                gotoLogin(getLoginUrl(), request, response);
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
