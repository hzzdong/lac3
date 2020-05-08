package com.linkallcloud.web.face.processor;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.web.face.annotation.Face;

public class OriginalProcessor extends RequestProcessor<String> {

    @Override
    protected ISessionUser doCheckLogin(String request, Face faceAnno, HttpServletRequest hsr) throws BaseException {
        return null;
    }

    @Override
    protected String doConvert2RealRequest(String content, Type type, Face faceAnno) {
        return content;
    }

    @Override
    public Object packageResult(Object message, Face faceAnno) throws BaseException {
        return message;
    }

    @Override
    public String getSignatureIdentity() {
        return null;
    }

}
