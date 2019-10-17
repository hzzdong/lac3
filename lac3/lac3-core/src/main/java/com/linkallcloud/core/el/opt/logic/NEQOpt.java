package com.linkallcloud.core.el.opt.logic;

import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * 不等于
 *
 */
public class NEQOpt extends TwoTernary {
    public int fetchPriority() {
        return 6;
    }
    public Object calculate() {
        Object lval = calculateItem(this.left);
        Object rval = calculateItem(this.right);
        if (lval == null || rval == null)
            return false;
        if(lval == rval){
            return false;
        }
        return !lval.equals(rval);
    }
    public String fetchSelf() {
        return "!=";
    }

}
