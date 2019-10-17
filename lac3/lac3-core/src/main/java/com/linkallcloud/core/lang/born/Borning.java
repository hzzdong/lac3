package com.linkallcloud.core.lang.born;

/**
 * 对象抽象创建方式
 * 
 * @param <T>
 */
public interface Borning<T> {

    T born(Object... args);

}
