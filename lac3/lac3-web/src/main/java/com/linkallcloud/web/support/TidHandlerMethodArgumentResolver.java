package com.linkallcloud.web.support;

import java.util.UUID;

import javax.servlet.ServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.trans.Tid;

public class TidHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Tid tid = parameter.getParameterAnnotation(Tid.class);

        String alias = getAlias(tid, parameter);

        Object target = null;
        if (mavContainer.containsAttribute(alias)) {
            target = mavContainer.getModel().get(alias);
        }

        if (target == null || !target.getClass().equals(String.class)) {
            ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
            target = servletRequest.getParameter(alias);
        }

        return (target == null || Strings.isBlank((String) target)) ? UUID.randomUUID().toString().replace("-", "")
                : target;
    }

    private String getAlias(Tid tid, MethodParameter parameter) {
        String alias = tid.value();
        if (alias == null || Strings.isBlank(alias)) {
            alias = "tid";
        }
        return alias;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Tid.class)
                || (parameter.getParameterType().equals(String.class) && "tid".equals(parameter.getParameterName()));
    }

}
