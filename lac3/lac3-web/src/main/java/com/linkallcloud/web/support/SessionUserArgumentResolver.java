package com.linkallcloud.web.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(SessionUser.class)
				|| parameter.getParameterType().isAssignableFrom(ISessionUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object target = Controllers.getSessionUser();
		return target;
	}

}
