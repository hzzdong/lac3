package com.linkallcloud.web.face.processor;

import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.core.exception.BaseException;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface IRequestProcessor {

    /**
     * 处理http post请求
     *
     * @param joinPoint
     * @param method
     * @param faceAnno
     * @return
     * @throws Throwable
     */
    Object handle(ProceedingJoinPoint joinPoint, Method method, Face faceAnno) throws Throwable;

    String getSignatureIdentity();// 签名者ID

    /**
     * 打包处理结果
     *
     * @param message
     * @param faceAnno
     * @return
     * @throws BaseException
     */
    Object packageResult(Object message, Face faceAnno) throws BaseException;

}
