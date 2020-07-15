package com.linkallcloud.core.aop;

import com.linkallcloud.core.lang.reflect.ReflectTool;

/**
 * 一个默认的类定义实现
 * 
 */
public class DefaultClassDefiner implements ClassDefiner {

    public static String debugDir;

    private static ClassDefiner me = new DefaultClassDefiner();

    public static ClassDefiner defaultOne() {
        return me;
    }

    public Class<?> define(String className, byte[] bytes, ClassLoader loader) {
        try {
            //if (DEBUG_DIR != null)
            //    Files.write(DEBUG_DIR + className.replace('.', '/') + ".class", bytes);
            return ReflectTool.defineClass(className, bytes, loader);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
