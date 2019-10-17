package com.linkallcloud.sh.sm;

import java.lang.reflect.Type;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.sh.Encrypts;
import com.linkallcloud.sh.tuils.Dates;

/**
 * 消息签名对象。支持消息自动加解密，暂时只支持AES。消息签名算法支持SHA-1和MD5，默认SHA-1。
 * 
 */
public class SignatureMessage implements ISignatureMessage {
    public static final long DEFAULT_TIMEOUT = 10000;// 默认超时时间：10秒

    private String message;// 原始消息内容
    private String messageEncAlg;// 消息加解密算法，暂时支持AES。
    private String timeStamp;// 时间戳
    private Signature signature;// 签名

    public SignatureMessage() {
        super();
    }

    /**
     * 把收到的完整的安全消息包转换成SignatureMessage对象
     * 
     * @param signatureMessage
     *            string
     * @return SignatureMessage object
     */
    public static SignatureMessage from(String signatureMessage) {
        return JSON.parseObject(signatureMessage, SignatureMessage.class);
    }

    /**
     * 构造消息签名对象，消息不加密，签名算法默认SHA-1。
     * 
     * @param messageObj
     *            原始消息对象
     * @param signatureIdentity
     *            签名者身份标识
     * @throws MsgException
     */
    public SignatureMessage(Object messageObj, String signatureIdentity) throws MsgException {
        this(messageObj, null, null, signatureIdentity, null);
    }

    /**
     * 构造消息签名对象，消息不加密。
     * 
     * @param messageObj
     *            原始消息对象
     * @param signatureIdentity
     *            签名者身份标识
     * @param signatureAlg
     *            签名算法
     * @throws MsgException
     */
    public SignatureMessage(Object messageObj, String signatureIdentity, String signatureAlg) throws MsgException {
        this(messageObj, null, null, signatureIdentity, signatureAlg);
    }

    /**
     * 构造消息签名对象，签名算法默认SHA-1。
     * 
     * @param messageObj
     *            原始消息对象
     * @param messageEncAlg
     *            消息加密算法，目前只支持：AES算法。
     * @param messageEncKey
     *            消息加密KEY
     * @param signatureIdentity
     *            签名者身份标识
     * @throws MsgException
     */
    public SignatureMessage(Object messageObj, String messageEncAlg, String messageEncKey, String signatureIdentity)
            throws MsgException {
        this(messageObj, messageEncAlg, messageEncKey, signatureIdentity, null);
    }

    /**
     * 构造消息签名对象
     * 
     * @param messageObj
     *            原始消息对象
     * @param messageEncAlg
     *            消息加密算法，目前只支持：DES,DESede,Blowfish三种算法。
     * @param messageEncKey
     *            消息加密KEY
     * @param signatureIdentity
     *            签名者身份标识
     * @param signatureAlg
     *            签名算法
     * @param offset
     *            偏移量
     * @throws MsgException
     */
    public SignatureMessage(Object messageObj, String messageEncAlg, String messageEncKey, String signatureIdentity,
            String signatureAlg) throws MsgException {
        this.messageEncAlg = messageEncAlg;
        this.message = encryptMessage(messageObj, messageEncAlg, messageEncKey);
        this.signature = new Signature(signatureIdentity, signatureAlg);

    }

    @Override
    @JSONField(serialize = false)
    public boolean isTimeout() {
        return isTimeout(DEFAULT_TIMEOUT);
    }

    @Override
    @JSONField(serialize = false)
    public boolean isTimeout(long period) {
        Date messageTime = Dates.parseDate(getTimeStamp());
        return new Date().getTime() - messageTime.getTime() > period;
    }

    @Override
    public void sign(String signatureKey) throws MsgException {
        if (message == null || message.length() <= 0) {
            throw new MsgException("410002", "\"message\" must not null.");
        }
        reSetTime();// 时间设置为当前时刻
        String hv = digestHMAC(getSignatureMethodAlgorithm(), signatureKey + getMessage() + getTimeStamp());
        getSignature().setValue(hv);
    }

    @Override
    public void verify(String signatureKey) throws MsgException {
        if (signatureKey == null || signatureKey.length() <= 0) {
            throw new MsgException("410004", "\"signatureKey\" must not null.");
        }
        if (message == null || message.length() <= 0) {
            throw new MsgException("410002", "\"message\" must not null.");
        }
        if (timeStamp == null || timeStamp.length() <= 0) {
            throw new MsgException("410003", "\"timeStamp\" must not null.");
        }
        if (getSignatureValue() == null) {
            throw new MsgException("420001", "Message must be signed.");
        }

        String hv = digestHMAC(getSignatureMethodAlgorithm(), signatureKey + getMessage() + getTimeStamp());
        if (hv == null || !hv.equals(getSignatureValue())) {
            throw new MsgException("420004", "Signature verfy failure.");
        }
    }

    @Override
    public void verifyStrict(String signatureIdentity, String signatureKey, String signatureMethodAlgorithm)
            throws MsgException {
        if (signatureKey == null || signatureKey.length() <= 0) {
            throw new MsgException("410004", "\"signatureKey\" must not null.");
        }
        if (message == null || message.length() <= 0) {
            throw new MsgException("410002", "\"message\" must not null.");
        }
        if (timeStamp == null || timeStamp.length() <= 0) {
            throw new MsgException("410003", "\"timeStamp\" must not null.");
        }

        Signature signature = getSignature();
        if (signature.getIdentity() == null || !signature.getIdentity().equals(signatureIdentity)) {
            throw new MsgException("420002", "\"signatureIdentity\" not match.");
        }

        if (signature.getAlgorithm() == null || !signature.getAlgorithm().equals(signatureMethodAlgorithm)) {
            throw new MsgException("420003", "\"signatureMethodAlgorithm\" not match.");
        }

        if (getSignatureValue() == null) {
            throw new MsgException("420001", "Message must be signed.");
        }

        String hv = digestHMAC(signatureMethodAlgorithm, signatureKey + getMessage() + getTimeStamp());
        if (hv == null || !hv.equals(getSignatureValue())) {
            throw new MsgException("420004", "Signature verfy failure.");
        }
    }

    @Override
    public String packMessage() throws MsgException {
        if (getSignatureValue() == null) {
            throw new MsgException("410001", "Message must be signed first.");
        }
        return JSON.toJSONString(this);
    }

    @Override
    public String packMessage(String signatureKey) throws MsgException {
        sign(signatureKey);
        return packMessage();
    }

    @Override
    public <T> T unpackMessage(Class<T> msgObjClass) throws MsgException {
        if (this.message == null || this.message.length() <= 0) {
            return null;
        }
        return JSON.parseObject(this.message, msgObjClass);
    }

    @Override
    public <T> T unpackMessage(Type msgObjType) throws MsgException {
        if (this.message == null || this.message.length() <= 0) {
            return null;
        }
        return JSON.parseObject(this.message, msgObjType);
    }

    @Override
    public <T> T unpackMessage(Class<T> msgObjClass, String messageEncKey) throws MsgException {
        if (this.message == null || this.message.length() <= 0) {
            return null;
        }
        if (null != messageEncKey && messageEncKey.length() > 0) {
            this.message = decryptMessage(this.message, messageEncKey);// 消息解码
        }
        return JSON.parseObject(this.message, msgObjClass);
    }

    @Override
    public <T> T unpackMessage(Type msgObjType, String messageEncKey) throws MsgException {
        if (this.message == null || this.message.length() <= 0) {
            return null;
        }
        if (null != messageEncKey && messageEncKey.length() > 0) {
            this.message = decryptMessage(this.message, messageEncKey);// 消息解码
        }
        return JSON.parseObject(this.message, msgObjType);
    }

    @Override
    public <T> T verifyAndUnpackMessage(Class<T> msgObjClass, String signatureKey, String messageEncKey)
            throws MsgException {
        verify(signatureKey);// 验证签名
        return unpackMessage(msgObjClass, messageEncKey);// 解包
    }

    @Override
    public <T> T verifyStrictAndUnpackMessage(Class<T> msgObjClass, String signatureIdentity, String signatureKey,
            String signatureMethodAlgorithm, String messageEncKey) throws MsgException {
        verifyStrict(signatureIdentity, signatureKey, signatureMethodAlgorithm);// 验证签名
        return unpackMessage(msgObjClass, messageEncKey);// 解包
    }

    @Override
    public <T> T verifyAndUnpackMessage(Type msgObjType, String signatureKey, String messageEncKey)
            throws MsgException {
        verify(signatureKey);// 验证签名
        return unpackMessage(msgObjType, messageEncKey);// 解包
    }

    @Override
    public <T> T verifyStrictAndUnpackMessage(Type msgObjType, String signatureIdentity, String signatureKey,
            String signatureMethodAlgorithm, String messageEncKey) throws MsgException {
        verifyStrict(signatureIdentity, signatureKey, signatureMethodAlgorithm);// 验证签名
        return unpackMessage(msgObjType, messageEncKey);// 解包
    }

    /**
     * 消息编码
     * 
     * @param messageObj
     *            原始消息对象
     * @param messageEncAlg
     *            消息加密算法
     * @param messageEncKey
     *            消息加密KEY
     * @param encryptor
     *            自定义的消息加解密器
     * @return 加密后的消息
     * @throws MsgException
     */
    private String encryptMessage(Object messageObj, String messageEncAlg, String messageEncKey) throws MsgException {
        // Gson gson = new GsonBuilder().create();
        // String jsonMessage = gson.toJson(messageObj);// JSON.toJSONString(messageObj);// JSON格式的原始消息
        this.messageEncAlg = messageEncAlg;
        String jsonMessage = JSON.toJSONString(messageObj);// JSON格式的原始消息

        if (messageEncAlg == null || messageEncAlg.isEmpty()) {
            this.message = jsonMessage;
        } else if ("AES".equals(messageEncAlg)) {
            try {
                this.message = Encrypts.encryptAES(jsonMessage, messageEncKey);
            } catch (Exception e) {
                throw new MsgException("430002", "AES encrypt error.");
            }
        } else {
            throw new MsgException("430001", "Message encryptor is only support for AES.");
        }
        return this.message;
    }

    /**
     * 消息解码
     * 
     * @param messageEncKey
     *            消息加密KEY
     * @param encryptor
     *            自定义的消息加解密器
     * @throws MsgException
     */
    private String decryptMessage(String message, String messageEncKey) throws MsgException {
        if (message == null || message.isEmpty()) {
            return null;
        }

        if (messageEncAlg == null || messageEncAlg.isEmpty() || messageEncKey == null || messageEncKey.isEmpty()) {
            throw new MsgException("420003", "Message encryptor is only support for AES.");
        } else if ("AES".equals(messageEncAlg)) {
            try {
                this.message = Encrypts.decryptAES(message, messageEncKey);
                return this.message;
            } catch (Exception e) {
                throw new MsgException("430002", "AES decrypt error.");
            }
        } else {
            throw new MsgException("430001", "Message encryptor is only support for AES.");
        }
    }

    /**
     * 签名
     * 
     * @param m
     * @return String
     * @throws MsgException
     */
    private String digestHMAC(String signatureMethodAlgorithm, String m) throws MsgException {
        try {
            return "MD5".equalsIgnoreCase(signatureMethodAlgorithm) ? Encrypts.md5Encrypt(m) : Encrypts.sha1Encrypt(m);
        } catch (Exception e) {
            throw new MsgException("420005", "签名错误");
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the messageEncAlg
     */
    @Override
    public String getMessageEncAlg() {
        return messageEncAlg;
    }

    public void setMessageEncAlg(String messageEncAlg) {
        this.messageEncAlg = messageEncAlg;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return the signature
     */
    public Signature getSignature() {
        if (null == signature) {
            signature = new Signature();
        }
        return signature;
    }

    @Override
    @JSONField(serialize = false)
    public String getSignatureIdentity() {
        return getSignature().getIdentity();
    }

    public void reSetTime() {
        this.timeStamp = Dates.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    @JSONField(serialize = false)
    public String getSignatureMethodAlgorithm() {
        return getSignature().getAlgorithm();
    }

    @Override
    @JSONField(serialize = false)
    public String getSignatureValue() {
        return getSignature().getValue();
    }

}
