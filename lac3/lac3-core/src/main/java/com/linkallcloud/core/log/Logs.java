package com.linkallcloud.core.log;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.log.impl.Log4jLogAdapter;
import com.linkallcloud.core.log.impl.NopLog;
import com.linkallcloud.core.log.impl.SystemLogAdapter;
import com.linkallcloud.core.plugin.SimplePluginManager;

/**
 * 获取 Log 的静态工厂方法
 */
public final class Logs {

    private static LogAdapter adapter;

    static {
        init();
    }

    /**
     * Get a Log by Class
     * 
     * @param clazz
     *            your class
     * @return Log
     */
    public static Log getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    /**
     * Get a Log by name
     * 
     * @param className
     *            the name of Log
     * @return Log
     */
    public static Log getLog(String className) {
        return adapter.getLogger(className);
    }

    /**
     * 返回以调用者的类命名的Log,是获取Log对象最简单的方法!
     */
    public static Log get() {
    	StackTraceElement[] sts = Thread.currentThread().getStackTrace();
    	if (Lang.isAndroid) {
    		for (int i = 0; i < sts.length; i++) {
				if (sts[i].getClassName().equals(Logs.class.getName())) {
					return adapter.getLogger(sts[i+1].getClassName());
				}
			}
    	}
    	return adapter.getLogger(sts[2].getClassName());
    }

    /**
     * 初始化Log,检查全部Log的可用性,选择可用的Log适配器
     * <p/>
     * <b>加载本类时,该方法已经在静态构造函数中调用,用户无需主动调用.</b>
     * <p/>
     * <b>除非迫不得已,请不要调用本方法<b/>
     * <p/>
     */
    public static void init() {
        try {
            String packageName = Logs.class.getPackage().getName() + ".impl.";
            adapter = new SimplePluginManager<LogAdapter>(
                    packageName + "CustomLogAdapter",
                    packageName + "Slf4jLogAdapter",
                    packageName + "Log4jLogAdapter",
                    packageName + "SystemLogAdapter").get();
        }
        catch (Throwable e) {
            try {
                Log4jLogAdapter tmp = new Log4jLogAdapter();
                if (tmp.canWork())
                    adapter = tmp;
                else
                    adapter = new SystemLogAdapter();
            } catch (Throwable _e) {
                adapter = new SystemLogAdapter();
            }
        }
    }
    
    /**
     * 开放自定义设置LogAdapter,注意,不能设置为null!! 如果你打算完全禁用log,可以设置为NOP_ADAPTER
     * @param adapter 你所偏好的LogAdapter
     */
    public static void setAdapter(LogAdapter adapter) {
		Logs.adapter = adapter;
	}
    
    /**
     * 什么都不做的适配器,无任何输出,某些人就想完全禁用掉Log,就可以用上它了
     */
    public static LogAdapter NOP_ADAPTER = NopLog.NOP;
}
