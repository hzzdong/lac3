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
            if (t != null && t instanceof UndeclaredThrowableException) {
                UndeclaredThrowableException ute = (UndeclaredThrowableException) t;
                Throwable uteThrow = ute.getUndeclaredThrowable();
                if (uteThrow == null) {
                    uteThrow = ute.getCause();
                }
                throw this.wrapeBaseException(uteThrow);
            } else {
                throw this.wrapeBaseException(t);
            }
        }
    }

    protected Throwable wrapeBaseException(Throwable t) {
        if (t == null) {
            return new BaseException(Exceptions.CODE_ERROR_UNKNOW, "未知错误，请联系管理员。");
        } else if (t instanceof BaseException) {
            return (BaseException) t;
        } else if (t instanceof BaseRuntimeException) {
            BaseRuntimeException exception = (BaseRuntimeException) t;
            return new BaseException(exception.getCode(), exception.getMessage());
        } else if (t instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException ute = (UndeclaredThrowableException) t;
            Throwable uteThrow = ute.getUndeclaredThrowable();
            if (uteThrow == null) {
                uteThrow = ute.getCause();
            }
            return new BaseException(Exceptions.CODE_ERROR_UNKNOW, uteThrow.getMessage());
        } else if (t instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) t;
            return new BaseException(Exceptions.CODE_ERROR_UNKNOW, exception.getMessage());
        } else {
            return new BaseException(Exceptions.CODE_ERROR_UNKNOW, t.getMessage());
        }
    }

}
