package com.linkallcloud.web.advice;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.WebUtils;

public class ExceptionHandlerAdvice {
    private static Log log = Logs.get();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleIllegalParamException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String tips = "参数不合法";
        if (errors.size() > 0) {
            tips = errors.get(0).getDefaultMessage();
        }

        log.warnf("uri={} | errorInfo={}", request.getRequestURI(), tips);
        log.error("MethodArgumentNotValidException", e);

        Map<String, Object> result = Exceptions.makeErrorMap(Exceptions.CODE_ERROR_PARAMETER, tips);
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public ModelAndView handleResultException(BaseRuntimeException e, HttpServletRequest request) {
        log.errorf("uri={} | errorInfo={}", request.getRequestURI(), e.getMessage());
        log.error("BaseRuntimeException", e);

        Map<String, Object> result = Exceptions.makeErrorMap(e.getCode(), e.getMessage());
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

    @ExceptionHandler(BaseException.class)
    public ModelAndView handleResultException(BaseException e, HttpServletRequest request) {
        log.errorf("uri={} | errorInfo={}", request.getRequestURI(), e.getMessage());
        log.error("BaseException", e);

        Map<String, Object> result = Exceptions.makeErrorMap(e.getCode(), e.getMessage());
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ModelAndView handleUndeclaredThrowableException(UndeclaredThrowableException ute,
            HttpServletRequest request) {
        log.errorf("uri={} | errorInfo={}", request.getRequestURI(), ute.getMessage());
        log.error("UndeclaredThrowableException", ute);

        Throwable re = ute.getUndeclaredThrowable();
        if (re == null) {
            re = ute.getCause();
        }

        Map<String, Object> result = Exceptions.makeErrorMap(re);
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
         log.errorf("uri={} | errorInfo={}", request.getRequestURI(), e.getMessage());

        // BusiLog busiLog = LacAspect.logMessageThreadLocal.get();
        // System.out.println(busiLog.getError() == e);
        log.error("Exception", e);

        Map<String, Object> result = Exceptions.makeErrorMap(e);
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(Throwable e, HttpServletRequest request) {
        log.errorf("uri={} | errorInfo={}", request.getRequestURI(), e.getMessage());
        log.error("Exception", e);

        Map<String, Object> result = Exceptions.makeErrorMap(e);
        if (WebUtils.isAjax(request)) {
            return new ModelAndView(new FastJsonJsonView(), result);
        }
        return new ModelAndView("page/system/error", result);
    }

}
