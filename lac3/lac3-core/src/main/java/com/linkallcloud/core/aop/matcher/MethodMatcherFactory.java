package com.linkallcloud.core.aop.matcher;

import com.linkallcloud.core.aop.MethodMatcher;

/**
 * 创建MethodMatcher的工厂类
 * 
 * @see MethodMatcher
 * 
 */
public final class MethodMatcherFactory {

    private MethodMatcherFactory() {}

    public static MethodMatcher matcher() {
        return matcher(-1);
    }

    public static MethodMatcher matcher(int mod) {
        return matcher(null, mod);
    }

    public static MethodMatcher matcher(String regex) {
        return matcher(regex, 0);
    }

    public static MethodMatcher matcher(String regex, int mod) {
        return matcher(regex, null, mod);
    }

    public static MethodMatcher matcher(String regex, String ignore, int mod) {
        return new RegexMethodMatcher(regex, ignore, mod);
    }
}
