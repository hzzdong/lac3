package com.linkallcloud.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.core.www.utils.WebUtils;

public class PermissionFilter extends LoginFilter {

	public PermissionFilter() {
		super();
		this.notFilter.add("/index");
	}

	public PermissionFilter(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	@Override
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
		response.sendRedirect(request.getContextPath() + getNoPermissionUrl());
	}

	private String getNoPermissionUrl() {
		return "/pub/noPermission";
	}

}
