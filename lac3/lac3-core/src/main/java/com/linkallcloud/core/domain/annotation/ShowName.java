package com.linkallcloud.core.domain.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Retention(RUNTIME)
@Target({ TYPE })
@Documented
public @interface ShowName {

    /**
     * Domain显示名称
     */
    public String value();
    
    /**
     * 记录日志的时候默认用哪几个字段的值表示某Domain
     * 
     * @return
     */
    public String logFields() default "id,name";

}
