package com.linkallcloud.core.busilog.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface ServLog {
	
	String desc() default ""; // 操作内容：支持thymeleaf语法

    boolean db() default false;// 是否要记录到数据库日志表

}
