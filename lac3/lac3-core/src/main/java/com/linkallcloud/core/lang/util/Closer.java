package com.linkallcloud.core.lang.util;

/**
 * 一个闭合器通用接口
 * 
 */
public interface Closer<T> {

    T invoke();

}
