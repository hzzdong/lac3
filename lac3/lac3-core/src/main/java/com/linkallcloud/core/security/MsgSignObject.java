package com.linkallcloud.core.security;

import java.io.Serializable;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class MsgSignObject implements Serializable {
    private static final long serialVersionUID = -6940179650417767658L;

    private static final Log log = Logs.get();

    private String messageEncAlg;// 消息加解密算法，暂时支持AES。
    private String messageEncKey;// 加密的秘钥

    private String signatureIdentity;// 签名者ID
    private String signatureAlg;// 签名算法
    private String signatureKey;// 签名的key

    private long timeout;

    public MsgSignObject() {
        super();
    }

    public MsgSignObject(String signatureIdentity, String signatureAlg, String signatureKey) {
        super();
        this.signatureIdentity = signatureIdentity;
        this.signatureAlg = signatureAlg;
        this.signatureKey = signatureKey;
    }

    public MsgSignObject(String signatureIdentity, String signatureAlg, String signatureKey, String messageEncAlg,
            String messageEncKey) {
        this(signatureIdentity, signatureAlg, signatureKey);
        if (!Strings.isBlank(messageEncAlg)) {
            if (!"AES".equals(messageEncAlg)) {
                log.warn("******************** 数据包加密算法目前只支持AES算法，已经帮您自动设置成AES，请知晓！！！ ********************");
            }
            this.messageEncAlg = "AES";
            this.messageEncKey = messageEncKey;
        }
    }

    public String getMessageEncAlg() {
        return messageEncAlg;
    }

    public void setMessageEncAlg(String messageEncAlg) {
        this.messageEncAlg = messageEncAlg;
    }

    public String getMessageEncKey() {
        return messageEncKey;
    }

    public void setMessageEncKey(String messageEncKey) {
        this.messageEncKey = messageEncKey;
    }

    public String getSignatureIdentity() {
        return signatureIdentity;
    }

    public void setSignatureIdentity(String signatureIdentity) {
        this.signatureIdentity = signatureIdentity;
    }

    public String getSignatureAlg() {
        return signatureAlg;
    }

    public void setSignatureAlg(String signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
