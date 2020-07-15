package com.linkallcloud.core.el.opt.arithmetic;

import java.util.Queue;

import com.linkallcloud.core.el.ElException;
import com.linkallcloud.core.el.opt.AbstractOpt;

/**
 * 右括号')'
 *
 */
public class RBracketOpt extends AbstractOpt {

    public int fetchPriority() {
        return 100;
    }
    public String fetchSelf() {
        return ")";
    }
    public void wrap(Queue<Object> obj) {
        throw new ElException("')符号不能进行wrap操作!'");
    }
    public Object calculate() {
        throw new ElException("')'符号不能进行计算操作!");
    }

}
