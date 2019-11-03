package com.linkallcloud.core.exception;

import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.aop.ThrowsAdvice;

public class BizExceptionFilter<T extends BizException> implements ThrowsAdvice {

    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    public BizExceptionFilter() {
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    public void afterThrowing(Throwable e) throws Throwable {
        //业务异常时直接抛出
        if (mirror.getType().isAssignableFrom(e.getClass())) {
            throw (T) e;
        }
        if (mirror.getType().equals(e.getClass())) {
            throw (T) e;
        }
        //BaseException
        else if (e instanceof BizException) {
            BizException be = (BizException) e;
            throw mirror.born(be.getCode(), be.getMessage());
        }
        //dubbo服务异常时封装成子类异常抛出
        else if (e instanceof RpcException) {
            RpcException rcpe = (RpcException) e;
            throw mirror.born("rcp-" + rcpe.getCode(), rcpe.getMessage());
        }
        //BaseException
        else if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            throw mirror.born(be.getCode(), be.getMessage());
        }
        //BaseException
        else if (e instanceof BaseRuntimeException) {
            BaseRuntimeException bre = (BaseRuntimeException) e;
            throw mirror.born(bre.getCode(), bre.getMessage());
        }
        //其它系统异常时封装成子类异常抛出
        else {
            throw mirror.born(Exceptions.CODE_ERROR_UNKNOW, e.getMessage());
        }
    }

}
