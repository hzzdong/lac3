package com.linkallcloud.web.filter;


import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.mds.DBContextHolder;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class DataSourceFilter implements Filter {

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        SessionUser user = (SessionUser) Controllers.getSessionUser(getAppCode(), request);
        if (user != null && !Strings.isBlank(user.getOrgDataSource())) {
            DBContextHolder.setDBType(user.getOrgDataSource());
        }

        chain.doFilter(request, res);
    }

    protected abstract String getAppCode();

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

}
