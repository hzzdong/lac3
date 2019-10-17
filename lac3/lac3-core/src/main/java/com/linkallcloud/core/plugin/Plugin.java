package com.linkallcloud.core.plugin;

/**
 * 插件 -- 一个通用的扩展点
 * 
 */
public interface Plugin {

    /**
     * @return 当前插件是否能正常工作
     */
    boolean canWork();

}
