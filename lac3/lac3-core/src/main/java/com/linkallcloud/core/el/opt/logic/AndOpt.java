package com.linkallcloud.core.el.opt.logic;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.el.opt.TwoTernary;

/**
 * and
 * 
 */
public class AndOpt extends TwoTernary {
    public int fetchPriority() {
        return 11;
    }

    public Object calculate() {
        Object lval = calculateItem(this.left);
        if (null == lval)
            return false;

        if (!(lval instanceof Boolean)) {
            // throw new ElException("操作数类型错误!");
            return Castors.me().castTo(lval, Boolean.class);
        }
        if (!(Boolean) lval) {
            return false;
        }

        Object rval = calculateItem(this.right);
        if (null == rval)
            return false;
        if (!(rval instanceof Boolean)) {
            // throw new ElException("操作数类型错误!");
            return Castors.me().castTo(rval, Boolean.class);
        }
        if (!(Boolean) rval) {
            return false;
        }
        return true;
    }

    public String fetchSelf() {
        return "&&";
    }

}
