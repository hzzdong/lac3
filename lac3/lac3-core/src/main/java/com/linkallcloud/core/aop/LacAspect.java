package com.linkallcloud.core.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.domain.IDomain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.service.IService;
import com.linkallcloud.core.thymeleaf.Templates;
import com.linkallcloud.core.trans.Tid;

public abstract class LacAspect {

    protected Log log = Logs.get();

    protected static LocalVariableTableParameterNameDiscoverer parameterNameDiscovere =
            new LocalVariableTableParameterNameDiscoverer();

    /**
     * 获取当前执行的方法
     *
     * @param joinPoint
     *            连接点
     * @return 方法
     */
    protected Method getMethod(JoinPoint joinPoint) {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        try {
            return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        } catch (SecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 处理TID，根据@Tid注解或者参数名字为"tid",或者类型是Trace
     * 
     * @param joinPoint
     * @param method
     * @param ifNullGenerateTid
     * @return
     */
    protected String dealTid(ProceedingJoinPoint joinPoint, Method method, boolean ifNullGenerateTid) {
        String[] parameterNames = parameterNameDiscovere.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        method.setAccessible(true);
        Trace trace = null;
        Parameter[] parameters = method.getParameters();
        for (int j = 0; j < parameters.length; j++) {
            Parameter parameter = parameters[j];
            Tid tid = parameter.getAnnotation(Tid.class);
            if (tid != null) {
                return Castors.me().castToString(args[j]);
            }
            if (parameter.getParameterizedType().getTypeName().equals(Trace.class.getName())) {
                trace = (Trace) args[j];
            }
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if ("tid".equals(parameterNames[i])) {
                return Castors.me().castToString(args[i]);
            }
        }

        String tid = null;

        if (trace != null) {
            tid = trace.getTid();
        }

        if (Strings.isBlank(tid) && ifNullGenerateTid) {
            tid = UUID.randomUUID().toString().replace("-", "");
        }

        if (trace != null) {
            trace.setTid(tid);
        } else {
            trace = new Trace(tid);
        }

        return tid;
    }

    /**
     * Thymeleaf 模板
     * 
     * @param web
     * @param template
     * @param joinPoint
     * @param method
     * @param values
     * @return
     */
    protected String dealStringTtemplate(boolean web, String template, ProceedingJoinPoint joinPoint, Method method,
            Map<String, Object> values) {
        String[] parameterNames = parameterNameDiscovere.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        if (parameterNames == null || parameterNames.length == 0 || args == null || args.length == 0) {
            return template;
        }

        if (!Strings.isBlank(template)) {
            Map<String, Object> context = new HashMap<String, Object>();
            for (int i = 0; i < parameterNames.length; i++) {
                context.put(parameterNames[i], args[i]);
            }
            if (values != null && !values.isEmpty()) {
                context.putAll(values);
            }
            DomainDescription dd = getDomainDescription(joinPoint);
            if (dd != null) {
                context.put("domainShowName", dd.getShowName());
            }
            return Templates.processStringTemplate(template, context);
        } else {
            DomainDescription dd = getDomainDescription(joinPoint);
            if (web) {
                // 用户([(${visitor.name})])[# th:if=\"${entity.id!=null &&
                // entity.id>0}\"]修改了[/][# th:if=\"${entity.id==null ||
                // entity.id==0}\"]新增了[/]菜单([(${entity.name})]), TID:[(${tid})]
                int saveTag = (int) values.get("saveTag");
                if (saveTag == 1) {
                    return dealWebInsertOrUpdateLogDesc(true, (String) values.get("tid"),
                            (AppVisitor) values.get("visitor"), args, dd);
                } else if (saveTag == 2) {
                    return dealWebInsertOrUpdateLogDesc(false, (String) values.get("tid"),
                            (AppVisitor) values.get("visitor"), args, dd);
                }
            } else {
                if (dd != null && method.getName().equals("insert")) {
                    // String desc = "新增 [(${domainShowName})]([(${entity.name})]), TID:[(${tid})]";
                    return dealServiceInsertOrUpdateLogDesc(true, (String) values.get("tid"), args, dd);
                } else if (dd != null && method.getName().equals("update")) {
                    // 修改 [(${domainShowName})]([(${entity.name})],[(${entity.id})]), TID:[(${tid})]
                    return dealServiceInsertOrUpdateLogDesc(false, (String) values.get("tid"), args, dd);
                }
            }
        }
        return null;
    }

    /**
     * 处理新增日志描述信息
     * 
     * @param insert
     * @param tid
     * @param visitor
     * @param methodArgs
     * @param dd
     * @return
     */
    private String dealWebInsertOrUpdateLogDesc(boolean insert, String tid, AppVisitor visitor, Object[] methodArgs,
            DomainDescription dd) {
        StringBuffer descBuffer = new StringBuffer();
        descBuffer.append("用户(").append(visitor == null ? "" : visitor.getLoginName()).append(")")
                .append(insert ? "新增了 " : "修改了 ").append(dd.getShowName());

        if (dd.getLogFields() != null && dd.getLogFields().length > 0) {
            descBuffer.append("(");

            IDomain<?> domain = null;
            Mirror<?> domainMirror = null;
            for (Object arg : methodArgs) {
                domainMirror = Mirror.me(arg);
                if (domainMirror.is(dd.getDomainClass())) {
                    domain = (IDomain<?>) arg;
                    break;
                }
            }

            if (domain != null) {
                boolean first = true;
                for (String logField : dd.getLogFields()) {
                    Object fieldValue = domainMirror.getValue(domain, logField);
                    String fieldValueStr = fieldValue == null ? "" : Castors.me().castToString(fieldValue);
                    if (!first) {
                        descBuffer.append(",");
                    }
                    first = false;
                    descBuffer.append(logField).append("=").append(fieldValueStr);
                }
            }

            descBuffer.append(")");
        }

        descBuffer.append(", TID:").append(tid).append(".");
        return descBuffer.toString();
    }

    /**
     * 处理新增日志描述信息
     * 
     * @param tid
     * @param methodArgs
     * @param dd
     * @return
     */
    private String dealServiceInsertOrUpdateLogDesc(boolean insert, String tid, Object[] methodArgs,
            DomainDescription dd) {
        StringBuffer descBuffer = new StringBuffer();
        descBuffer.append(insert ? "新增 " : "修改 ").append(dd.getShowName());

        if (dd.getLogFields() != null && dd.getLogFields().length > 0) {
            descBuffer.append("(");

            IDomain<?> domain = null;
            Mirror<?> domainMirror = null;
            for (Object arg : methodArgs) {
                domainMirror = Mirror.me(arg);
                if (domainMirror.is(dd.getDomainClass())) {
                    domain = (IDomain<?>) arg;
                    break;
                }
            }

            if (domain != null) {
                boolean first = true;
                for (String logField : dd.getLogFields()) {
                    Object fieldValue = domainMirror.getValue(domain, logField);
                    String fieldValueStr = fieldValue == null ? "" : Castors.me().castToString(fieldValue);
                    if (!first) {
                        descBuffer.append(",");
                    }
                    first = false;
                    descBuffer.append(logField).append("=").append(fieldValueStr);
                }
            }

            descBuffer.append(")");
        }

        descBuffer.append(", TID:").append(tid).append(".");
        return descBuffer.toString();
    }

    /**
     * 从service中得到domain的showname
     * 
     * @param joinPoint
     * @return
     */
    @SuppressWarnings("unchecked")
    protected DomainDescription getDomainDescription(ProceedingJoinPoint joinPoint) {
        Class<?> clzz = joinPoint.getTarget().getClass();
        Mirror<?> cmirror = Mirror.me(clzz);
        Class<? extends IDomain<?>> domainClass = null;
        if (cmirror.isOf(IService.class)) {// service
            IService<?, ? extends IDomain<?>> service = (IService<?, ? extends IDomain<?>>) joinPoint.getTarget();
            domainClass = service.getDomainClass();
        } else if (clzz.getAnnotation(Controller.class) != null) {// Controller
            Method method = null;
            try {
                method = clzz.getMethod("getDomainClass");
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e) {
            }
            if (method != null) {
                try {
                    domainClass = (Class<? extends IDomain<?>>) method.invoke(joinPoint.getTarget());
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }

        if (domainClass != null) {
            if (domainClass.isAnnotationPresent(ShowName.class)) {
                ShowName sn = domainClass.getAnnotation(ShowName.class);
                if (sn != null) {
                    return new DomainDescription(domainClass, sn.value(), sn.logFields());
                }
            }
            return new DomainDescription(domainClass, domainClass.getSimpleName(), "id,name");
        }
        return null;
    }

    /**
     * 得到模块名称定义
     * 
     * @param cl
     * @return
     */
    protected String getModuleName(Class<?> cl) {
        if (cl.isAnnotationPresent(Module.class)) {
            Module m = cl.getAnnotation(Module.class);
            if (m != null) {
                return m.name();
            }
        }
        return null;
    }

    protected Object[] getValidateArgs(ProceedingJoinPoint joinPoint) {
        List<Object> vargs = new ArrayList<Object>();
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (Object arg : joinPoint.getArgs()) {
                if (arg == null || arg instanceof ServletRequest || arg instanceof HttpSession
                        || arg instanceof ServletResponse) {
                    //
                } else {
                    vargs.add(arg);
                }
            }
        }
        return vargs.toArray();
    }

}
