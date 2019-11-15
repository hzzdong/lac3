package com.linkallcloud.web.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class LacErrorController extends BasicErrorController {
	private static Log log = Logs.get();

	public LacErrorController() {
		super(new DefaultErrorAttributes(), new ErrorProperties());
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections
				.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView != null) ? modelAndView : new ModelAndView("page/system/error", model);
	}

	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		if (status == HttpStatus.NO_CONTENT) {
			return new ResponseEntity<Map<String, Object>>(status);
		}

		Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		String code = "500";
		if (body.get("code") != null) {
			code = String.valueOf(body.get("code"));
		}
		String message = String.valueOf(body.get("message"));
		body.forEach((k, v) -> log.infof("%s ==> %s", k, v));
		Map<String, Object> result = Exceptions.makeErrorMap(code, message);
		return new ResponseEntity<>(result, status);
	}

}
