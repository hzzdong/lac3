package com.linkallcloud.web.filter;

import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.web.session.SessionUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class PermissionFilter extends LacCommonFilter {

    public PermissionFilter() {
        super();
    }

    public PermissionFilter(List<String> ignoreRes, boolean override) {
        super(ignoreRes, override);
    }

    protected abstract String getAppCode();

    @Override
    protected void doConcreteFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionUser u = getLoginUser(getAppCode(), request);
        if (null != u) {
            doCheckPermission(u, request, response, chain);
        }
    }

    protected void doCheckPermission(SessionUser suser, HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        if (suser != null) {
            if (suser.getLoginName().equals("superadmin")) {
                filterChain.doFilter(request, response);
                return;
            } else {
                String url = WebUtils.getRequestUrl(request);
                if (suser.hasMenuPermission(url)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        throw new BizException("PermissionFilter", "您没有权限访问此资源");
    }

}
