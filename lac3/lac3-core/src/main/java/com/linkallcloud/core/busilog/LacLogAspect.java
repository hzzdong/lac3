package com.linkallcloud.core.busilog;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.linkallcloud.core.aop.LacAspect;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.busilog.util.LogExceptionStackTrace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Stopwatch;

public abstract class LacLogAspect<T extends LacBusiLog> extends LacAspect {
    protected Mirror<T> logMirror;

    @SuppressWarnings("unchecked")
    public LacLogAspect() {
        super();
        try {
            logMirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    protected abstract void logStorage(T operatelog) throws Exception;

    protected abstract void subclassDealLogs(T operatelog, JoinPoint joinPoint, Class<?> clzz, Method method,
            LacLog logAnnot);

    /**
     * 保存系统操作日志
     *
     * @param joinPoint
     *            连接点
     * @return 方法执行结果
     * @throws Throwable
     *             调用出错
     */
    protected Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        BusiLog busiLog = dealThreadLocalBaseLog(joinPoint, method);
        T operatelog = null;
        Stopwatch sw = Stopwatch.begin();
        Object result = "";

        LacLog logAnnot = method.getAnnotation(LacLog.class);
        if (logAnnot == null) {
            try {
                sw.start();
                return joinPoint.proceed();
            } catch (Throwable e) {
                sw.stop();
                operatelog = logMirror.born();
                org.springframework.beans.BeanUtils.copyProperties(busiLog, operatelog);
                autoDealLogs(operatelog, sw.getDuration(), joinPoint, method, logAnnot, e);
                throw e;
            }
        } else {
            operatelog = logMirror.born();
            org.springframework.beans.BeanUtils.copyProperties(busiLog, operatelog);

            try {
                sw.start();
                result = joinPoint.proceed();
                sw.stop();
                try {
                    operatelog.setMethodResult(result);
                    autoDealLogs(operatelog, sw.getDuration(), joinPoint, method, logAnnot, null);
                } catch (Throwable e1) {
                    log.error(e1.getMessage(), e1);
                }
            } catch (Throwable e) {
                sw.stop();
                try {
                    autoDealLogs(operatelog, sw.getDuration(), joinPoint, method, logAnnot, e);
                } catch (Throwable e1) {
                    log.error(e1.getMessage(), e1);
                }
                throw e;
            }
        }

        return result;
    }

    private void autoDealLogs(T operatelog, long duration, ProceedingJoinPoint joinPoint, Method method,
            LacLog logAnnot, Throwable e) {
        Class<?> clzz = joinPoint.getTarget().getClass();

        String tid = dealTid(joinPoint, method, false);
        operatelog.setTid(tid);
        operatelog.setModule(getModuleName(clzz));
        operatelog.setCostTime(duration);
        operatelog.setOperateResult(e == null ? 0 : 1);

        if (e != null) {
            String errorMessage = LogExceptionStackTrace.erroStackTrace(e).toString();
            operatelog.setErrorMessage(errorMessage);
        }
        operatelog.setError(e);

        subclassDealLogs(operatelog, joinPoint, clzz, method, logAnnot);

        // log.info(JSON.toJSONString(operatelog, true));
        try {
            if (operatelog.getOperateTime() == null) {
                operatelog.setOperateTime(new Date());
            }
            operatelog.setDtTime(operatelog.getOperateTime().getTime());

            logStorage(operatelog);
            operatelog.setAlreadyStored(true);
            logMessageThreadLocal.set(operatelog);
        } catch (Throwable er) {
            log.error(er.getMessage(), er);
        }
    }

}
