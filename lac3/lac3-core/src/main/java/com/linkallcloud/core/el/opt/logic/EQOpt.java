package com.linkallcloud.core.el.opt.logic;

import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * 等于
 * 
 */
public class EQOpt extends TwoTernary {
    public int fetchPriority() {
        return 7;
    }

    public Object calculate() {
        Object lval = calculateItem(this.left);
        Object rval = calculateItem(this.right);
        if (lval == null && rval == null)
            return true;

        if (lval == null || rval == null)
            return false;

        if (lval == rval) {
            return true;
        }
        return lval.equals(rval);
    }

    public String fetchSelf() {
        return "==";
    }
}
