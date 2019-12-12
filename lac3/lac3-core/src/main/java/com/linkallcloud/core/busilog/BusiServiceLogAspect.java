package com.linkallcloud.core.busilog;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.aop.LacAspect;
import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.ServiceBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Stopwatch;
import com.linkallcloud.core.manager.IServiceBusiLogManager;
import com.linkallcloud.core.service.IService;
import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

public abstract class BusiServiceLogAspect<T extends ServiceBusiLog, TS extends IServiceBusiLogManager<T>>
        extends LacAspect {

    protected Mirror<T> logMirror;

    @SuppressWarnings("unchecked")
    public BusiServiceLogAspect() {
        super();
        try {
            logMirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[1]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    protected abstract TS logService();

    /**
     * 保存系统操作日志
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 调用出错
     */
    public Object autoLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);
        ServLog logAnnot = method.getAnnotation(ServLog.class);
        if (logAnnot == null) {
            return joinPoint.proceed();
        }

        Class<?> clzz = joinPoint.getTarget().getClass();
        Stopwatch sw = Stopwatch.begin();
        Object result = "";

        T operatelog = logMirror.born();
        // operatelog.setTid(tid);
        operatelog.setClassName(clzz.getName());
        operatelog.setMethodName(joinPoint.getSignature().getName());
        // operatelog.setMethodParameters(getValidateArgs(joinPoint));
        operatelog.setMethodParameters(JSON.toJSONString(getValidateArgs(joinPoint)));

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
        return result;
    }

    private void autoDealLogs(T operatelog, long duration, ProceedingJoinPoint joinPoint, Method method,
                              ServLog logAnnot, Throwable e) {
        Class<?> clzz = joinPoint.getTarget().getClass();
        Mirror<?> cmirror = Mirror.me(clzz);
        if (cmirror.isOf(IService.class)) {
            /* busi */
            String tid = dealTid(joinPoint, method, false);
            operatelog.setTid(tid);

            operatelog.setOperateDesc(
                    dealStringTtemplate(false, logAnnot.desc(), joinPoint, method, new HashMap<String, Object>() {
                        private static final long serialVersionUID = 1L;

                        {
                            put("tid", tid);
                        }
                    }));

            operatelog.setModule(getModuleName(clzz));
            // operatelog.setOperateDesc(operationDesc);

            operatelog.setOperateTime(new Date());
            operatelog.setCostTime(duration);
            operatelog.setOperateResult(e == null ? 0 : 1);
            if (e != null) {
                String errorMessage = e.getMessage();
                if (errorMessage != null && errorMessage.length() > 512) {
                    errorMessage = errorMessage.substring(0, 512);
                }
                operatelog.setErrorMessage(errorMessage);
            }

            /* service */
            String clientIP = RpcContext.getContext().getRemoteHost();
            operatelog.setIp(clientIP);

            // operatelog.setClassName(clzz.getName());
            // operatelog.setMethodName(joinPoint.getSignature().getName());
            // operatelog.setMethodParameters(getValidateArgs(joinPoint));
            // operatelog.setMethodResult(result);
            operatelog.setError(e);

            log.info(JSON.toJSONString(operatelog, true));

            if (logService() != null && logAnnot.db()) {
                try {
                    logService().insert(new Trace(tid), operatelog);
                } catch (Throwable er) {
                    log.error(er.getMessage(), er);
                }
            }
        }
    }

}
