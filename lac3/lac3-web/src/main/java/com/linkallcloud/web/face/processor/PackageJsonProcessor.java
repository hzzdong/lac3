package com.linkallcloud.web.face.processor;

import com.linkallcloud.sh.sm.ISignatureMessage;
import com.linkallcloud.sh.sm.MsgException;
import com.linkallcloud.sh.sm.SignatureMessage;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.response.BlankFaceResponse;
import com.linkallcloud.core.face.message.response.FaceResponse;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.MsgSignObject;

import java.lang.reflect.Type;

public abstract class PackageJsonProcessor extends PackageRequestProcessor {

    public PackageJsonProcessor() {
        super();
    }

    @Override
    protected FaceMessage doConvert2RealRequest(String messagePkg, Type type, Face faceAnno)
            throws BaseException {
        ISignatureMessage receiveSM = SignatureMessage.from(messagePkg);
        if (receiveSM == null) {
            log.error("400004：数据包格式错误。");
            throw new BaseException("400004", "数据包格式错误。");
        }
        this.setSignatureIdentity(receiveSM.getSignatureIdentity());

        MsgSignObject mso = getMsgSignObject(receiveSM.getSignatureIdentity());
        try {
            receiveSM.verifyStrict(mso.getSignatureIdentity(), mso.getSignatureKey(),
                    Strings.isBlank(faceAnno.signatureAlg()) ? mso.getSignatureAlg() : faceAnno.signatureAlg());// 验证签名
        } catch (MsgException e) {
            log.errorf("%s：%s", e.getCode(), e.getMessage());
            throw new BaseException(e.getCode(), e.getMessage());
        }
        if (receiveSM.isTimeout(mso.getTimeout() * 1000)) {// 验证超时
            log.error("400006：数据包超时错误。");
            throw new BaseException("400006", "数据包超时错误。");
        }

        FaceMessage result = null;
        if (Strings.isBlank(faceAnno.requestEncAlg())) {// 本方法没有指定加密算法，以message中指定的加密算法处理。
            if (!Strings.isBlank(receiveSM.getMessageEncAlg()) && !Strings.isBlank(mso.getMessageEncKey())) {
                try {
                    result = (FaceMessage) receiveSM.unpackMessage(type, mso.getMessageEncKey());// 解包出原始消息
                } catch (MsgException e) {
                    log.errorf("%s：%s", e.getCode(), e.getMessage());
                    throw new BaseException(e.getCode(), e.getMessage());
                }
                log.debug("接收到的数据包解密后：" + receiveSM.getMessage());
            } else {
                try {
                    result = (FaceMessage) receiveSM.unpackMessage(type);// 解包出原始消息
                } catch (MsgException e) {
                    log.errorf("%s：%s", e.getCode(), e.getMessage());
                    throw new BaseException(e.getCode(), e.getMessage());
                }
                log.debug("接收到的数据包解密后：" + receiveSM.getMessage());
            }
        } else if (faceAnno.requestEncAlg().equals(receiveSM.getMessageEncAlg())) {// 判断message中的加密算法是否和方法制定的加密算法一致
            try {
                result = (FaceMessage) receiveSM.unpackMessage(type, mso.getMessageEncAlg());// 解包出原始消息
            } catch (MsgException e) {
                log.errorf("%s：%s", e.getCode(), e.getMessage());
                throw new BaseException(e.getCode(), e.getMessage());
            }
            result.setAppCode(mso.getSignatureIdentity());
            log.debug("接收到的数据包解密后：" + receiveSM.getMessage());
        } else {// 不一致，抛错
            log.error("400007：数据包中数据加密算不符合制定要求");
            throw new BaseException("400007", "数据包中数据加密算不符合制定要求");
        }

        if (result != null) {
            result.setAppCode(receiveSM.getSignatureIdentity());
        }
        return result;
    }

    @Override
    public Object packageResult(Object message, Face faceAnno) throws BaseException {
        FaceResponse response = null;
        if (message == null) {
            response = new BlankFaceResponse();
        } else if (message.getClass().getName().equals("java.lang.String")) {
            String temp = (String) message;
            if (Strings.isBlank(temp)) {
                response = new BlankFaceResponse();
            } else {
                response = new ObjectFaceResponse<Object>(message);
            }
        } else if (!(message instanceof FaceMessage)) {
            response = new ObjectFaceResponse<Object>(message);
        } else {
            response = (FaceResponse) message;
        }

        try {
            MsgSignObject mso = getMsgSignObject(this.getSignatureIdentity());
            if (mso != null) {
                response.setAppCode(mso.getSignatureIdentity());
                String salg = Strings.isBlank(faceAnno.signatureAlg()) ? mso.getSignatureAlg()
                        : faceAnno.signatureAlg();
                ISignatureMessage responsePackage = new SignatureMessage(response, faceAnno.responseEncAlg(),
                        mso.getMessageEncKey(), this.getSignatureIdentity(), salg);
                responsePackage.sign(mso.getSignatureKey());// 签名
                return responsePackage;
            }
        } catch (MsgException e) {
            log.errorf("%s：%s", e.getCode(), e.getMessage());
            throw new BaseException(e.getCode(), e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new BaseException("400007", e.getMessage());
        }

        return message;
    }

}
