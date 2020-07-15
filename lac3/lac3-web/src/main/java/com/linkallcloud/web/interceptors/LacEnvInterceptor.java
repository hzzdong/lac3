package com.linkallcloud.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public abstract class LacEnvInterceptor extends HandlerInterceptorAdapter {

    protected abstract String getStaticServer();

    protected abstract String getResourceVersion();

    //protected abstract String getServer();
    //protected abstract String getFullContextPath();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("static_server", getStaticServer());
        request.setAttribute("static_res_version", getResourceVersion());
        //request.setAttribute("server", getServer());
        //request.setAttribute("f_ctx", getFullContextPath());
        return super.preHandle(request, response, handler);
    }

}
