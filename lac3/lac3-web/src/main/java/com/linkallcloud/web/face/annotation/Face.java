package com.linkallcloud.web.face.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Face {

    FdMode mode() default FdMode.JSON;

    // 请求加密算法：不加密为null。
    String requestEncAlg() default "";

    // 响应加密算法，不加密为null。
    String responseEncAlg() default "";

    // 签名的H算法
    String signatureAlg() default "SHA1";

    /*-
     * true:  简单模式，不用签名，InputStrim转json(xml)，符合RequestFace格式;
     * false: 通常模式，需要签名，InputStrim转json(xml)，符合ISignatureMessage格式，解析出来的原始消息符合RequestFace格式;
     */
    boolean simple() default false;

    /*-
     * 是否需要登录验证
     */
    boolean login() default true;
}
