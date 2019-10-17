package com.linkallcloud.sh.sm;

import java.lang.reflect.Type;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 消息签名对象。支持消息自动加解密(暂时只支持DES,DESede,Blowfish)，其它加解密算法请自行提供加解密器。 消息签名算法支持SHA-1和MD5，默认SHA-1。
 * 
 */
public interface ISignatureMessage {

    /**
     * 得到原始消息内容
     * 
     * @return
     */
    String getMessage();

    /**
     * 原始数据的加密算法
     * 
     * @return
     */
    String getMessageEncAlg();

    /**
     * 得到时间戳
     * 
     * @return
     */
    String getTimeStamp();

    /**
     * 得到签名算法
     * 
     * @return
     */
    @JSONField(serialize = false)
    String getSignatureMethodAlgorithm();

    /**
     * 得到签名者身份标识
     * 
     * @return
     */
    @JSONField(serialize = false)
    String getSignatureIdentity();

    /**
     * 得到签名内容
     * 
     * @return
     */
    @JSONField(serialize = false)
    String getSignatureValue();

    /**
     * 是否超时:有效期默认为10秒
     * 
     * @return
     */
    @JSONField(serialize = false)
    boolean isTimeout();

    /**
     * 是否超时：有效期为period
     * 
     * @return
     */
    @JSONField(serialize = false)
    boolean isTimeout(long period);

    /**
     * 签名
     * 
     * @param signatureKey
     *            签名的共享密钥
     * @throws MsgException
     */
    void sign(String signatureKey) throws MsgException;

    /**
     * 验证签名
     * 
     * @param signatureKey
     *            签名的共享密钥
     * @throws MsgException
     */
    void verify(String signatureKey) throws MsgException;

    /**
     * 验证签名
     * 
     * @param signatureIdentity
     *            签名者
     * @param signatureKey
     *            签名的共享密钥
     * @param signatureMethodAlgorithm
     *            签名算法
     * @throws MsgException
     */
    void verifyStrict(String signatureIdentity, String signatureKey, String signatureMethodAlgorithm)
            throws MsgException;

    /**
     * 签名后调用此函数打包成完整消息包
     * 
     * @return
     * @throws MsgException
     */
    String packMessage() throws MsgException;

    /**
     * 未签名，可直接调用此函数自动签名并打包成完整消息包
     * 
     * @param signatureKey
     *            签名的共享密钥
     * @return
     * @throws MsgException
     */
    String packMessage(String signatureKey) throws MsgException;

    /**
     * 验证成功后调用此函数解包出消息并转化成java对象，本方法只适用于消息没有加密的情况
     * 
     * @param msgObjClass
     *            消息对象类型
     * @return
     * @throws MsgException
     */
    <T> T unpackMessage(Class<T> msgObjClass) throws MsgException;

    /**
     * 验证成功后调用此函数解包出消息并转化成java对象，本方法只适用于消息没有加密的情况
     * 
     * @param msgObjType
     *            消息对象类型
     * @return
     * @throws MsgException
     */
    <T> T unpackMessage(Type msgObjType) throws MsgException;

    /**
     * 验证成功后调用此函数解包出消息并转化成java对象
     * 
     * @param msgObjClass
     *            消息对象类型
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T unpackMessage(Class<T> msgObjClass, String messageEncKey) throws MsgException;

    /**
     * 验证成功后调用此函数解包出消息并转化成java对象
     * 
     * @param msgObjType
     *            消息对象类型
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T unpackMessage(Type msgObjType, String messageEncKey) throws MsgException;

    /**
     * 未验证，可调用此函数自动验证、解包出原始消息并转化成java对象
     * 
     * @param msgObjClass
     *            消息对象类型
     * @param signatureKey
     *            签名的共享密钥
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T verifyAndUnpackMessage(Class<T> msgObjClass, String signatureKey, String messageEncKey) throws MsgException;

    /**
     * 未验证，可调用此函数自动验证、解包出原始消息并转化成java对象
     * 
     * @param msgObjClass
     *            消息对象类型
     * @param signatureIdentity
     *            签名者ID
     * @param signatureKey
     *            签名的共享密钥
     * @param signatureMethodAlgorithm
     *            签名算法
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T verifyStrictAndUnpackMessage(Class<T> msgObjClass, String signatureIdentity, String signatureKey,
            String signatureMethodAlgorithm, String messageEncKey) throws MsgException;

    /**
     * 未验证，可调用此函数自动验证、解包出原始消息并转化成java对象
     * 
     * @param msgObjType
     *            消息对象类型
     * @param signatureKey
     *            签名的共享密钥
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T verifyAndUnpackMessage(Type msgObjType, String signatureKey, String messageEncKey) throws MsgException;

    /**
     * 未验证，可调用此函数自动验证、解包出原始消息并转化成java对象
     * 
     * @param msgObjType
     *            消息对象类型
     * @param signatureIdentity
     *            签名者ID
     * @param signatureKey
     *            签名的共享密钥
     * @param signatureMethodAlgorithm
     *            签名算法
     * @param messageEncKey
     *            消息加密KEY
     * @return
     * @throws MsgException
     */
    <T> T verifyStrictAndUnpackMessage(Type msgObjType, String signatureIdentity, String signatureKey,
            String signatureMethodAlgorithm, String messageEncKey) throws MsgException;

}
