package com.linkallcloud.web.face.processor;

import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.security.MsgSignObject;

public abstract class PackageRequestProcessor extends LoginRequestProcessor {

    // private String messageEncAlg;// 消息加解密算法，暂时支持AES。
    // private String messageEncKey;// 加密的秘钥
    // private String signatureIdentity;// 签名者ID
    // private String signatureAlg;// 签名算法
    // private String signatureKey;// 签名的key
    // private long timeout;
    // private String offset;

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
        /**
         * ThreadLocal没有被当前线程赋值时或当前线程刚调用remove方法后调用get方法，返回此方法值
         */
        @Override
        protected String initialValue() {
            return null;
        }
    };
    private Map<String, MsgSignObject> msgSignInfo = new HashMap<>();

    public PackageRequestProcessor() {
        super();
    }

    public Map<String, MsgSignObject> getMsgSignInfo() {
        return msgSignInfo;
    }

    public void addMsgSignObject(String sId, MsgSignObject mso) {
        msgSignInfo.put(sId, mso);
    }

    public void setMsgSignInfo(Map<String, MsgSignObject> msgSignInfo) {
        this.msgSignInfo = msgSignInfo;
    }

    public MsgSignObject getMsgSignObject(String signatureIdentity) throws BaseException {
        MsgSignObject mso = msgSignInfo.get(signatureIdentity);
        if (mso == null) {
            throw new BaseException("400010", "找不到接口配置参数或配置错误");
        }
        return mso;
    }

    public String getSignatureIdentity() {
        return PackageRequestProcessor.threadLocal.get();
    }

    public void setSignatureIdentity(String signatureIdentity) {
        PackageRequestProcessor.threadLocal.set(signatureIdentity);
    }

}
