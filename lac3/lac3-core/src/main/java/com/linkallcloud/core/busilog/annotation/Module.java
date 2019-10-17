package com.linkallcloud.core.busilog.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE })
@Documented
public @interface Module {

    /**
     * 模塊名字
     * 
     * @return
     */
    public String name();

}
