package com.linkallcloud.core.el.opt.custom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.linkallcloud.core.el.opt.RunMethod;
import com.linkallcloud.core.plugin.Plugin;

/**
 * 时间函数
 */
public class TimeNow implements RunMethod, Plugin {

    public boolean canWork() {
        return true;
    }

    public Object run(List<Object> fetchParam) {
        if (fetchParam == null || fetchParam.isEmpty())
            return System.currentTimeMillis();
        return new SimpleDateFormat(fetchParam.get(0).toString()).format(new Date());
    }

    public String fetchSelf() {
        return "now";
    }
}
