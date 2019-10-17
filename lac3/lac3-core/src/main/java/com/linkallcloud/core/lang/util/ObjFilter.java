package com.linkallcloud.core.lang.util;

/**
 * 通用过滤器
 * 
 * @param <T>
 */
public interface ObjFilter<T> {

    boolean accept(T o);

}
