package com.linkallcloud.core.el.opt.logic;

import java.util.Queue;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.el.opt.AbstractOpt;

/**
 * Not(!)
 * 
 */
public class NotOpt extends AbstractOpt {
    private Object right;

    public int fetchPriority() {
        return 7;
    }

    public void wrap(Queue<Object> rpn) {
        right = rpn.poll();
    }

    public Object calculate() {
        Object rval = calculateItem(this.right);
        if (null == rval)
            return true;
        if (rval instanceof Boolean) {
            return !(Boolean) rval;
        }
        // throw new ElException("'!'操作符操作失败!");
        return !Castors.me().castTo(rval, Boolean.class);
    }

    public String fetchSelf() {
        return "!";
    }
}
