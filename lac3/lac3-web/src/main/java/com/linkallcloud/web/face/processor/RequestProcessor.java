package com.linkallcloud.web.face.processor;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.sh.sm.MsgException;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.util.HibernateValidator;
import com.linkallcloud.core.www.utils.InputStreamUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public abstract class RequestProcessor<T> implements IRequestProcessor {
    protected static Log log = Logs.get();
    protected static LocalVariableTableParameterNameDiscoverer parameterNameDiscovere = new LocalVariableTableParameterNameDiscoverer();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public RequestProcessor() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    @Override
    public Object handle(ProceedingJoinPoint joinPoint, Method method, Face faceAnno) throws Throwable {
        HttpServletRequest hsr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.debug("接收到的请求URL：" + hsr.getRequestURL());

        String messagePkg = null;
        try {
            messagePkg = InputStreamUtils.convertStreamToString(hsr.getInputStream());
            log.debug("接收到的数据包：" + messagePkg);
            if (Strings.isBlank(messagePkg)) {
                log.error("400003：从Request读取InputStream，结果为空。");
                throw new BaseException("400003", "从Request读取InputStream，结果为空。");
            }
        } catch (Throwable e) {
            log.error("400002：从Request读取InputStream出错", e);
            throw new BaseException("400002", "从Request读取InputStream出错");
        }

        try {
            // Class<?>[] argTypes = method.getParameterTypes();
            Type[] argTypes = method.getGenericParameterTypes();
            Object[] args = joinPoint.getArgs();
            boolean findT = false;
            SessionUser suser = null;
            int suserIdx = -1;
            int appVisitorIdx = -1;
            int tIdx = -1;
            Trace t = new Trace(true);
            if (argTypes != null && argTypes.length > 0) {
                for (int i = 0; i < argTypes.length; i++) {
                    Mirror<?> argMirror = Mirror.me(argTypes[i]);
                    if (argMirror.isOf(mirror.getType())) {
                        findT = true;
                        // T rmReq = doConvert2RealRequest(messagePkg, (Class<T>) argTypes[i],
                        // faceAnno);
                        T rmReq = doConvert2RealRequest(messagePkg, argTypes[i], faceAnno);
                        if (rmReq instanceof FaceRequest) {
                            FaceRequest req = (FaceRequest) rmReq;
                            if (req.getT() == null) {
                                req.setT(t);
                            } else {
                                t = req.getT();
                            }
                        }

                        if (faceAnno.login()) {// 需要登录校验
                            suser = doCheckLogin(rmReq, faceAnno);
                        }
                        beanValidator(rmReq);
                        args[i] = rmReq;
                    }
                    if (argMirror.isOf(SessionUser.class)) {
                        suserIdx = i;
                    }
                    if (argMirror.isOf(AppVisitor.class)) {
                        appVisitorIdx = i;
                    }
                    if (argMirror.isOf(Trace.class)) {
                        tIdx = i;
                    }
                }
                if (!findT) {
                    log.error("400005：Face Controller方法参数错误");
                    throw new BaseException("400005", "Face Controller方法参数错误，参数中必须有一个是T类型");
                }
                if (suser != null && suserIdx != -1) {
                    args[suserIdx] = suser;
                }
                if (suser != null && appVisitorIdx != -1) {
                    args[appVisitorIdx] = Controllers.getAppVisitor(suser);
                }
                if (tIdx != -1) {
                    args[tIdx] = t;
                }
                return doHandle(joinPoint, method, faceAnno, args);
            } else {
                log.error("400005：FaceController方法参数错误");
                throw new BaseException("400005", "FaceController方法参数错误");
            }
        } catch (MsgException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getCode(), e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected T doConvert2RealRequest(String content, Type type, Face faceAnno) throws BaseException {
        return Castors.me().castTo(content, (Class<T>) type.getClass());
    }

    protected SessionUser doCheckLogin(T request, Face faceAnno) throws BaseException {
        return null;
    }

    protected Object doHandle(ProceedingJoinPoint joinPoint, Method method, Face faceAnno, Object[] args)
            throws Throwable {
        return joinPoint.proceed(args);
    }

    protected void beanValidator(Object object) {
        HibernateValidator.getInstance().validatorThrowException(object);
    }

}
