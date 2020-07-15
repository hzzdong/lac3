package com.linkallcloud.core.el.opt.custom;

import java.util.List;

import com.linkallcloud.core.el.ElException;
import com.linkallcloud.core.el.opt.RunMethod;
import com.linkallcloud.core.plugin.Plugin;

/**
 * 去掉字符串两边的空格
 *
 */
public class Trim implements RunMethod, Plugin{
    public Object run(List<Object> fetchParam) {
        if(fetchParam.size() <= 0){
            throw new ElException("trim方法参数错误");
        }
        String obj = (String) fetchParam.get(0);
        return obj.trim();
    }

    public boolean canWork() {
        return true;
    }

    public String fetchSelf() {
        return "trim";
    }
}
