package com.linkallcloud.web.busilog;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.aop.DomainDescription;
import com.linkallcloud.core.aop.LacAspect;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.domain.IDomain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.laclog.WebBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Stopwatch;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.manager.IWebBusiLogManager;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

public abstract class BusiWebLogAspect<T extends WebBusiLog, TS extends IWebBusiLogManager<T>>
        extends LacAspect {
    protected Mirror<T> logMirror;

    @SuppressWarnings("unchecked")
    public BusiWebLogAspect() {
        super();
        try {
            logMirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
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
        WebLog logAnnot = method.getAnnotation(WebLog.class);
        if (logAnnot == null) {
            return joinPoint.proceed();
        }

        Class<?> clzz = joinPoint.getTarget().getClass();
        Stopwatch sw = Stopwatch.begin();
        Object result = "";

        String tid = dealTid(joinPoint, method, true);
        T operatelog = logMirror.born();
        operatelog.setTid(tid);

        if (!Strings.isBlank(logAnnot.desc())) {// 用户自定义
            AppVisitor visitor = Controllers.getAppVisitor();
            SessionUser su = Controllers.getSessionUser();
            operatelog.setOperateDesc(
                    dealStringTtemplate(true, logAnnot.desc(), joinPoint, method, new HashMap<String, Object>() {
                        private static final long serialVersionUID = 1L;

                        {
                            put("tid", tid);
                            put("su", su);
                            put("visitor", visitor);
                            put("saveTag", 0);
                        }
                    }));
        } else {
            operatelog.setSaveTag(getSaveMethodTag(joinPoint, method));
        }

        operatelog.setClassName(clzz.getName());
        operatelog.setMethodName(joinPoint.getSignature().getName());
        operatelog.setMethodParameters(JSON.toJSONString(getValidateArgs(joinPoint)));

        try {
            sw.start();
            result = joinPoint.proceed();
            sw.stop();
            try {
                operatelog.setMethodResult(result);
                autoDealLogs(tid, operatelog, sw.getDuration(), joinPoint, method, logAnnot, null);
            } catch (Throwable e1) {
                log.error(e1.getMessage(), e1);
            }
        } catch (Throwable e) {
            sw.stop();
            try {
                autoDealLogs(tid, operatelog, sw.getDuration(), joinPoint, method, logAnnot, e);
            } catch (Throwable e1) {
                log.error(e1.getMessage(), e1);
            }
            throw e;
        }
        return result;
    }

    /**
     * 得到save方法是否是新增还是更新的标志
     *
     * @param joinPoint
     * @param method
     * @return save4NewTag ,是否save方法，若是的话1：新增；2：更新，其它0
     */
    private int getSaveMethodTag(ProceedingJoinPoint joinPoint, Method method) {
        DomainDescription dd = getDomainDescription(joinPoint);
        if (dd != null && method.getName().startsWith("save")) {
            Mirror<?> domainMirror = null;
            for (Object arg : joinPoint.getArgs()) {
                domainMirror = Mirror.me(arg);
                if (domainMirror.is(dd.getDomainClass())) {
                    return getDomainSaveTage((IDomain)arg, domainMirror);
                } else if(domainMirror.is(ObjectFaceRequest.class)) {
                    Object data = ((ObjectFaceRequest<?>)arg).getData();
                    if(data instanceof IDomain) {
                        return getDomainSaveTage((IDomain)data, null);
                    }
                }
            }
        }
        return 0;
    }

    private int getDomainSaveTage(IDomain domain, Mirror<?> domainMirror) {
        int save4NewTag = 0;
        if (domain != null) {
            if(domainMirror == null) {
                domainMirror = Mirror.me(domain);
            }
            Object fieldValue = domainMirror.getValue(domain, "id");
            if (fieldValue == null) {
                save4NewTag = 1;
            } else if (fieldValue.getClass().equals(Long.class)) {
                Long id = (Long) fieldValue;
                if (id.longValue() <= 0) {
                    save4NewTag = 1;
                } else {
                    save4NewTag = 2;
                }
            } else {
                save4NewTag = 2;
            }
        }
        return save4NewTag;
    }

    private void autoDealLogs(String tid, T operatelog, long duration, ProceedingJoinPoint joinPoint, Method method,
                              WebLog logAnnot, Throwable e) {
        Class<?> clzz = joinPoint.getTarget().getClass();
        if (clzz.getAnnotation(Controller.class) != null) {
        	SessionUser su = Controllers.getSessionUser();
            AppVisitor visitor = Controllers.getAppVisitor();

            /* busi */
            if (Strings.isBlank(logAnnot.desc())) {// 用户没有定义，自动处理
                // operatelog.setTid(tid);
                operatelog.setOperateDesc(
                        dealStringTtemplate(true, null, joinPoint, method, new HashMap<String, Object>() {
                            private static final long serialVersionUID = 1L;

                            {
                                put("tid", tid);
                                put("su", su);
                                put("visitor", visitor);
                                put("saveTag", operatelog.getSaveTag());
                            }
                        }));
            }

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

            /* web */
            operatelog.setOperator(su);
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            operatelog.setIp(WebUtils.getIpAddress(request));
            operatelog.setUrl(request.getRequestURI());

            // operatelog.setClassName(clzz.getName());
            // operatelog.setMethodName(joinPoint.getSignature().getName());
            // operatelog.setMethodParameters(getValidateArgs(joinPoint));//TODO
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
