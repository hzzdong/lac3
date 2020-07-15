package com.linkallcloud.core.exception;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 包装Exception为Un Checked Exception，确保事物回滚
 */
public class WrapUnCheckedExceptionAspect {

    public Object wrapException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable t) {
            throw this.wrapeBaseException(t);
        }
    }

    protected Throwable wrapeBaseException(Throwable t) {
        if (t == null) {
            return new BaseRuntimeException(Exceptions.CODE_ERROR_UNKNOW, "未知错误，请联系管理员。");
        } else if (t instanceof BaseRuntimeException) {
            return (BaseRuntimeException) t;
        } else if (t instanceof BaseException) {
            BaseException exception = (BaseException) t;
            return new BaseRuntimeException(exception.getCode(), exception.getMessage());
        } else if (t instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException ute = (UndeclaredThrowableException) t;
            Throwable uteThrow = ute.getUndeclaredThrowable();
            if (uteThrow == null) {
                uteThrow = ute.getCause();
            }
            return new BaseRuntimeException(Exceptions.CODE_ERROR_UNKNOW, uteThrow.getMessage());
        } else if (t instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) t;
            return new BaseRuntimeException(Exceptions.CODE_ERROR_UNKNOW, exception.getMessage());
        } else {
            return new BaseRuntimeException(Exceptions.CODE_ERROR_UNKNOW, t.getMessage());
        }
    }

}
