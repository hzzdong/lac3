/**
 * Copyright (c) 2010 www.public.zj.cn
 *
 * cn.zj.pubinfo.web.filter.EncodingFilter.java 
 *
 * Created at 2010-7-2 by zhoudong(hzzdong@gmail.com)
 * 
 */
package com.linkallcloud.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoudong(hzzdong@gmail.com)
 * 
 */
public class EncodingFilter implements Filter {
	private String encoding = "utf-8";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain fc) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		HttpServletResponse hresponse = (HttpServletResponse) response;
		hresponse.setHeader("Cache-Control", "No-Cache");
		// response.setContentType("text/text; charset=utf-8");
		// response.addHeader("Cache-Control", "no-cache,must-revalidate");
		// response.addHeader("Pragma", "no-cache");
		fc.doFilter(request, hresponse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig fc) throws ServletException {
		String s = fc.getInitParameter("Encoding");
		if (s != null) {
			this.encoding = s;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

}
