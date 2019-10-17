package com.linkallcloud.core.el.opt.arithmetic;

import java.util.Queue;

import com.linkallcloud.core.el.ElException;
import com.linkallcloud.core.el.opt.AbstractOpt;

/**
 * "("
 *
 */
public class LBracketOpt extends AbstractOpt {
    public String fetchSelf() {
        return "(";
    }
    public int fetchPriority() {
        return 100;
    }
    
    public void wrap(Queue<Object> obj) {
        throw new ElException("'('符号不能进行wrap操作!");
    }
    public Object calculate() {
        throw new ElException("'('符号不能进行计算操作!");
    }
}
