package com.linkallcloud.web.face.processor;

import java.lang.reflect.Method;

import com.linkallcloud.web.utils.Controllers;
import org.aspectj.lang.ProceedingJoinPoint;

import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.LoginFaceRequest;
import com.linkallcloud.core.lang.Strings;

public abstract class LoginRequestProcessor extends RequestProcessor<FaceMessage> {

    @Override
    protected SessionUser doCheckLogin(FaceMessage request, Face faceAnno) throws BaseException {
        if (faceAnno.login()) {// 需要登录校验
            if (request instanceof FaceRequest) {
                FaceRequest req = (FaceRequest) request;
                String token = req.getToken();
                if (Strings.isBlank(token)) {
                    throw new BaseException("400008", "此方法需要登录校验，所以请求参数必须包含令牌信息。");
                } else {
                    SessionUser suser = Controllers.checkToken(token);// bfc.getSessionUser(sessionId);
                    if (suser == null || Strings.isBlank(suser.getUserType())
                            || Strings.isBlank(suser.getLoginName())) {
                        throw new BaseException("400009", "用户未登录或者登录失效，请重新发起登录验证。");
                    }
                    if (request instanceof LoginFaceRequest) {
                        LoginFaceRequest lreq = (LoginFaceRequest) request;
                        lreq.setUserType(suser.getUserType());
                        lreq.setUserName(suser.getName());
                        lreq.setUserId(Long.parseLong(suser.getId()));
                        lreq.setCompanyId(Long.parseLong(suser.getCompanyId()));
                        lreq.setCompanyName(suser.getCompanyName());
                    }
                    return suser;
                }
            } else {
                throw new BaseException("400008", "此方法需要登录校验，所以请求参数必须包含令牌信息。");
            }
        }
        return null;
    }

    @Override
    protected Object doHandle(ProceedingJoinPoint joinPoint, Method method, Face faceAnno, Object[] args)
            throws Throwable {
        return joinPoint.proceed(args);
    }

}
