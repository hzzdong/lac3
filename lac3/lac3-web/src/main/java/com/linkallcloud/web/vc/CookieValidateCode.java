package com.linkallcloud.web.vc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.core.lang.Strings;

public class CookieValidateCode extends ValidateCode {

	public CookieValidateCode() {
		super();
	}

	public CookieValidateCode(boolean numeric) {
		super(numeric);
	}

	public CookieValidateCode(boolean numeric, int securityCodeLength, int imageWidth, int imageHight) {
		super(numeric, securityCodeLength, imageWidth, imageHight);
	}

	@Override
	protected void storageSecurityCode(HttpServletRequest request, HttpServletResponse response, String code) {
		try {
			Cookie cookie = new Cookie(SECURITY_CODE_KEY, code);
			cookie.setSecure(request.isSecure());
			cookie.setMaxAge(-1);
			cookie.setPath(request.getContextPath());
			// cookie.setHttpOnly(true);
			response.addCookie(cookie);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	protected String getStoragedSecurityCode(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(SECURITY_CODE_KEY)) {
					String old = cookies[i].getValue();
					if (Strings.isBlank(old)) {
						continue;
					}
					storageSecurityCode(request, response, "");
					return old;
				}
			}
		}
		return null;
	}

}
