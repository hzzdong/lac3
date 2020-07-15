package com.linkallcloud.web.vc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionValidateCode extends ValidateCode {

	public SessionValidateCode() {
		super();
	}

	public SessionValidateCode(boolean numeric) {
		super(numeric);
	}

	public SessionValidateCode(boolean numeric, int securityCodeLength, int imageWidth, int imageHight) {
		super(numeric, securityCodeLength, imageWidth, imageHight);
	}

	@Override
	protected void storageSecurityCode(HttpServletRequest request, HttpServletResponse response, String code) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(SECURITY_CODE_KEY, code);
		}
	}

	@Override
	protected String getStoragedSecurityCode(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session != null) {
			String vcode = (String) session.getAttribute(SECURITY_CODE_KEY);
			session.setAttribute(SECURITY_CODE_KEY, "");
			return vcode;
		}
		return null;
	}

}
