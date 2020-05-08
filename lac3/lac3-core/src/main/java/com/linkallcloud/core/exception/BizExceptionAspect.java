package com.linkallcloud.core.exception;

import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.apache.dubbo.rpc.RpcException;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 包装Exception为BizException
 */
public class BizExceptionAspect<T extends BizException> {

    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
	public BizExceptionAspect() {
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

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
                throw wrapeBizException(uteThrow);
            } else {
                throw wrapeBizException(t);
            }
        }
    }

    @SuppressWarnings("unchecked")
	protected T wrapeBizException(Throwable e) {
        if (e == null) {
            return mirror.born(Exceptions.CODE_ERROR_UNKNOW, "未知错误，请联系管理员。");
        }
        //业务异常时直接抛出
        else if (mirror.getType().isAssignableFrom(e.getClass())) {
            return (T) e;
        } else if (mirror.getType().equals(e.getClass())) {
            return (T) e;
        } else if (e instanceof BizException) {
            BizException bize = (BizException) e;
            return mirror.born(bize.getCode(), bize.getMessage());
        }
        //dubbo服务异常时封装成子类异常抛出
        else if (e instanceof RpcException) {
            RpcException rcpe = (RpcException) e;
            return mirror.born("rcp-" + rcpe.getCode(), rcpe.getMessage());
        } else if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            return mirror.born(be.getCode(), be.getMessage());
        } else if (e instanceof BaseRuntimeException) {
            BaseRuntimeException bre = (BaseRuntimeException) e;
            return mirror.born(bre.getCode(), bre.getMessage());
        } else if (e instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException ute = (UndeclaredThrowableException) e;
            Throwable uteThrow = ute.getUndeclaredThrowable();
            if (uteThrow == null) {
                uteThrow = ute.getCause();
            }
            return mirror.born(Exceptions.CODE_ERROR_UNKNOW, uteThrow.getMessage());
        } else {
            return mirror.born(Exceptions.CODE_ERROR_UNKNOW, e.getMessage());
        }
    }
}
