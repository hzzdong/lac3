package com.linkallcloud.web.face.processor;

import java.lang.reflect.Type;

import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.core.exception.BaseException;

public class OriginalProcessor extends RequestProcessor<String> {

    @Override
    protected SessionUser doCheckLogin(String request, Face faceAnno) throws BaseException {
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
