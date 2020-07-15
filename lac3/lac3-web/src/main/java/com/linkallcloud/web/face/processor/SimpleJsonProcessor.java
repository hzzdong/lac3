package com.linkallcloud.web.face.processor;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.response.BlankFaceResponse;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;

public class SimpleJsonProcessor extends LoginRequestProcessor {

    @Override
    protected FaceMessage doConvert2RealRequest(String content, Type type, Face faceAnno)
            throws BaseException {
        FaceMessage fr = (FaceMessage) JSON.parseObject(content, type);
        return fr;
    }

    @Override
    public Object packageResult(Object message, Face faceAnno) throws BaseException {
        if (message == null) {
            message = new BlankFaceResponse();
        } else if (message.getClass().getName().equals("java.lang.String")) {
            String temp = (String) message;
            if (Strings.isBlank(temp)) {
                message = new BlankFaceResponse();
            } else {
                message = new ObjectFaceResponse<Object>(message);
            }
        } else if (!(message instanceof FaceMessage)) {
            message = new ObjectFaceResponse<Object>(message);
        }
        return message;
    }

    @Override
    public String getSignatureIdentity() {
        return null;
    }

}
