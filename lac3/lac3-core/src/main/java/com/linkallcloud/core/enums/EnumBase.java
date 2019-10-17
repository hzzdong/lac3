package com.linkallcloud.core.enums;

public interface EnumBase<T> {
    
    /**
     * 获取枚举代码
     *
     * @return
     */
    public T getCode();

    /**
     * 获取枚举信息
     *
     * @return
     */
    public String getMessage();

}
