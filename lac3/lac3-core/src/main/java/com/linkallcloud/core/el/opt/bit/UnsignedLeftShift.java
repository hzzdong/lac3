package com.linkallcloud.core.el.opt.bit;

import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * 无符号右移
 *
 */
public class UnsignedLeftShift extends TwoTernary {
    public int fetchPriority() {
        return 5;
    }
    public Object calculate() {
        Integer lval = (Integer) calculateItem(left);
        Integer rval = (Integer) calculateItem(right);
        return lval >>> rval;
    }
    public String fetchSelf() {
        return ">>>";
    }
}
