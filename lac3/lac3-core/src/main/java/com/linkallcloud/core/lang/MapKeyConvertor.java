package com.linkallcloud.core.lang;

/**
 * Lang.convertMapKey 的回调
 * 
 */
public interface MapKeyConvertor {

    /**
     * @param key
     *            原始的 key
     * @return 新的 key
     */
    String convertKey(String key);

}
