package com.linkallcloud.core.el.opt.logic;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * or(||)
 * 
 */
public class OrOpt extends TwoTernary {

    public int fetchPriority() {
        return 12;
    }

    public Object calculate() {
        Object lval = calculateItem(left);
        if (null != lval) {
            if (!(lval instanceof Boolean)) {
                // throw new ElException("操作数类型错误!");
                return Castors.me().castTo(lval, Boolean.class);
            }
            if ((Boolean) lval) {
                return true;
            }
        }
        Object rval = calculateItem(right);
        if (null != rval) {
            if (!(rval instanceof Boolean)) {
                // throw new ElException("操作数类型错误!");
                return Castors.me().castTo(rval, Boolean.class);
            }
            if ((Boolean) rval) {
                return true;
            }
        }
        return false;
    }

    public String fetchSelf() {
        return "||";
    }

}
