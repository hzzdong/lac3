package com.linkallcloud.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class SimpleFilter implements Filter {

    protected abstract String getStaticServer();

    protected abstract String getResourceVersion();

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
        request.setAttribute("static_server", getStaticServer());
        request.setAttribute("static_res_version", getResourceVersion());
        //request.setAttribute("server", getServer());
        //request.setAttribute("f_ctx", getFullContextPath());

        chain.doFilter(request, res);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

}
