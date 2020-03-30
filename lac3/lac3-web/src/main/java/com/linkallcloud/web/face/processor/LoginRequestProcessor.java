package com.linkallcloud.web.face.processor;

import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.LoginFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public abstract class LoginRequestProcessor extends RequestProcessor<FaceMessage> {

    @Override
    protected ISessionUser doCheckLogin(FaceMessage request, Face faceAnno, HttpServletRequest hsr) throws BaseException {
        if (faceAnno.login()) {// 需要登录校验
            if (request instanceof FaceRequest) {
                FaceRequest req = (FaceRequest) request;
                ISessionUser suser = getSessionUserByFaceRequest(req, hsr);
                if (suser == null) {
                    throw new BaseException("400008", "此方法需要登录校验，所以请求参数必须包含令牌信息。");
                } else {
                    if (request instanceof LoginFaceRequest) {
                        LoginFaceRequest lreq = (LoginFaceRequest) request;
                        lreq.setUserType(suser.getUserType());
                        lreq.setUserName(suser.getName());
                        lreq.setUserId(Long.parseLong(suser.getId()));
                        lreq.setCompanyId(Long.parseLong(suser.getCompanyId()));
                        lreq.setCompanyName(suser.getCompanyName());
                    }
                }
                return suser;
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

    /**
     * @param fr
     * @param hsr
     * @return
     * @throws BizException
     */
    protected ISessionUser getSessionUserByFaceRequest(FaceRequest fr, HttpServletRequest hsr) throws BizException {
        if (Strings.isBlank(fr.getAppCode())) {
            throw new BizException("e.face.login", "FaceRequest对象中缺少appCode参数");
        }
        ISessionUser suser = Controllers.getSessionUser(fr.getAppCode());
        if (suser == null) {
            String token = getLacToken(fr, hsr);
            if (!Strings.isBlank(token)) {
                suser = getSessionUserByToken(token);
            }
        }
        if (suser != null) {
            Controllers.login(fr.getAppCode(), suser);
        }
        return suser;
    }


    /**
     * 根据token加载用户SessionUser
     *
     * @param token
     * @return
     * @throws BizException
     */
    protected ISessionUser getSessionUserByToken(String token) throws BizException {
        if (!Strings.isBlank(token)) {
            try {
                SessionUser user = Controllers.checkToken(token);
                if (user != null) {
                    return user;
                }
            } catch (Throwable e) {
            }
        }
        throw new BizException("e.face.login", "根据token加载用户SessionUser发生错误：" + token);
    }

    protected String getLacToken(FaceRequest fr, HttpServletRequest hsr) {
        String token = fr.getToken();
        if (Strings.isBlank(token)) {
            token = hsr.getParameter("token");
        }
        if (Strings.isBlank(token)) {
            token = hsr.getHeader("token");
            try {
                if (!Strings.isBlank(token)) {
                    token = URLDecoder.decode(token, "UTF8");
                }
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
        }
        log.info("################ token:" + token);
        return token;
    }

}
