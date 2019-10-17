package com.linkallcloud.core.lang.util;

/**
 * 带三个参数的通用回调接口
 * 
 * @param <T0>
 * @param <T1>
 * @param <T2>
 */
public interface Callback3<T0, T1, T2> {

    void invoke(T0 arg0, T1 arg1, T2 arg2);

}
