package com.linkallcloud.core.el.opt.bit;

import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * 与
 *
 */
public class BitAnd extends TwoTernary {
    public int fetchPriority() {
        return 8;
    }
    public Object calculate() {
        Integer lval = (Integer) calculateItem(left);
        Integer rval = (Integer) calculateItem(right);
        return lval & rval;
    }
    public String fetchSelf() {
        return "&";
    }

}
