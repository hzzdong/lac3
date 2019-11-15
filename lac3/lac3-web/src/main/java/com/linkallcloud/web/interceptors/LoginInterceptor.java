package com.linkallcloud.web.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.web.session.SessionUser;

public abstract class LoginInterceptor extends PermissionInterceptor {

	public LoginInterceptor() {
		super();
	}

	public LoginInterceptor(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	public LoginInterceptor(List<String> ignoreRes, boolean override, String login, String noPermission) {
		super(ignoreRes, override, login, noPermission);
	}

	public LoginInterceptor(String login, String noPermission) {
		super(login, noPermission);
	}

	@Override
	protected boolean doCheckPermission(SessionUser suser, HttpServletRequest request, HttpServletResponse response,
                                        Object handler) throws Exception {
		return true;
	}

}
