package com.linkallcloud.web.face.processor;

import java.lang.reflect.Type;

import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.face.message.FaceMessage;

public class SimpleXmlProcessor extends LoginRequestProcessor {

    @Override
    protected FaceMessage doConvert2RealRequest(String xmlContent, Type type, Face faceAnno)
            throws BaseException {
        // TODO convert xmlContent to FaceMessage
        return null;
    }

    @Override
    public Object packageResult(Object message, Face faceAnno) throws BaseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSignatureIdentity() {
        return null;
    }

	@Override
	protected SessionUser getSessionUserBySimpleSessionUser(SimpleSessionUser simpleSessionUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
