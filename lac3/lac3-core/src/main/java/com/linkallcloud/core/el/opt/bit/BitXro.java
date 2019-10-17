package com.linkallcloud.core.el.opt.bit;

import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * 异或
 *
 */
public class BitXro extends TwoTernary {
    public int fetchPriority() {
        return 9;
    }
    public Object calculate() {
        Integer lval = (Integer) calculateItem(left);
        Integer rval = (Integer) calculateItem(right);
        return lval ^ rval;
    }
    public String fetchSelf() {
        return "^";
    }
}
