package com.linkallcloud.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.dto.Trace;

@Controller
public class ErrorThrowController {

	@RequestMapping(value = "/error/exthrow", method = { RequestMethod.GET, RequestMethod.POST })
	public Object error(Trace t, HttpServletRequest request, ModelMap modelMap) throws Exception {
		throw ((Exception) request.getAttribute("filter.error"));
	}

}
