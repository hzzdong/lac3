package com.linkallcloud.core.mapl;

/**
 * 将输入理解成Map+List
 */
public interface MaplCompile<T> {
    public Object parse(T t);
}
