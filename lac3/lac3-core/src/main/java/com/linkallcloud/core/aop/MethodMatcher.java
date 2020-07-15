package com.linkallcloud.core.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean match(Method method);

}