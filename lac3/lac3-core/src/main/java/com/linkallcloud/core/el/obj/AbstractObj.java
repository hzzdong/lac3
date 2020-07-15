package com.linkallcloud.core.el.obj;

import com.linkallcloud.core.el.ElCache;
import com.linkallcloud.core.lang.util.Context;

/**
 * 对象
 */
public class AbstractObj implements Elobj{
    private String val;
    private ElCache ec;
    public AbstractObj(String val) {
        this.val = val;
    }
    public String getVal() {
        return val;
    }
    public Object fetchVal(){
        Context context = ec.getContext();
        if(context != null && context.has(val)){
            return context.get(val);
        }
        return null;
    }
    public String toString() {
        return val;
    }
    public void setEc(ElCache ec) {
        this.ec = ec;
    }
}
