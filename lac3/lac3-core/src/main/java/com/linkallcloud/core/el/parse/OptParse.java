package com.linkallcloud.core.el.parse;

import com.linkallcloud.core.el.ElException;
import com.linkallcloud.core.el.Parse;
import com.linkallcloud.core.el.opt.object.AccessOpt;
import com.linkallcloud.core.el.opt.object.ArrayOpt;
import com.linkallcloud.core.el.opt.object.CommaOpt;
import com.linkallcloud.core.el.opt.object.FetchArrayOpt;
import com.linkallcloud.core.el.opt.arithmetic.DivOpt;
import com.linkallcloud.core.el.opt.arithmetic.LBracketOpt;
import com.linkallcloud.core.el.opt.arithmetic.ModOpt;
import com.linkallcloud.core.el.opt.arithmetic.MulOpt;
import com.linkallcloud.core.el.opt.arithmetic.PlusOpt;
import com.linkallcloud.core.el.opt.arithmetic.RBracketOpt;
import com.linkallcloud.core.el.opt.arithmetic.SubOpt;
import com.linkallcloud.core.el.opt.bit.BitAnd;
import com.linkallcloud.core.el.opt.bit.BitNot;
import com.linkallcloud.core.el.opt.bit.BitOr;
import com.linkallcloud.core.el.opt.bit.BitXro;
import com.linkallcloud.core.el.opt.bit.LeftShift;
import com.linkallcloud.core.el.opt.bit.RightShift;
import com.linkallcloud.core.el.opt.bit.UnsignedLeftShift;
import com.linkallcloud.core.el.opt.logic.AndOpt;
import com.linkallcloud.core.el.opt.logic.EQOpt;
import com.linkallcloud.core.el.opt.logic.GTEOpt;
import com.linkallcloud.core.el.opt.logic.GTOpt;
import com.linkallcloud.core.el.opt.logic.LTEOpt;
import com.linkallcloud.core.el.opt.logic.LTOpt;
import com.linkallcloud.core.el.opt.logic.NEQOpt;
import com.linkallcloud.core.el.opt.logic.NotOpt;
import com.linkallcloud.core.el.opt.logic.OrOpt;
import com.linkallcloud.core.el.opt.logic.QuestionOpt;
import com.linkallcloud.core.el.opt.logic.QuestionSelectOpt;

/**
 * 操作符转换器
 *
 */
public class OptParse implements Parse {

    public Object fetchItem(CharQueue exp){
        switch(exp.peek()){
        case '+':
            exp.poll();
            return new PlusOpt();
        case '-':
            exp.poll();
            return new SubOpt();
        case '*':
            exp.poll();
            return new MulOpt();
        case '/':
            exp.poll();
            return new DivOpt();
        case '%':
            exp.poll();
            return new ModOpt();
        case '(':
            exp.poll();
            return new LBracketOpt();
        case ')':
            exp.poll();
            return new RBracketOpt();
        case '>':
            exp.poll();
            switch(exp.peek()){
            case '=':
                exp.poll();
                return new GTEOpt();
            case '>':
                exp.poll();
                if(exp.peek() == '>'){
                    exp.poll();
                    return new UnsignedLeftShift();
                }
                return new RightShift();
            }
            return new GTOpt();
        case '<':
            exp.poll();
            switch(exp.peek()){
            case '=':
                exp.poll();
                return new LTEOpt();
            case '<':
                exp.poll();
                return new LeftShift();
            }
            return new LTOpt();
        case '=':
            exp.poll();
            switch(exp.peek()){
            case '=':
                exp.poll();
                return new EQOpt();
            }
            throw new ElException("表达式错误,请检查'='后是否有非法字符!");
        case '!':
            exp.poll();
            switch(exp.peek()){
            case '=':
                exp.poll();
                return new NEQOpt();
            }
            return new NotOpt();
        case '|':
            exp.poll();
            switch(exp.peek()){
            case '|':
                exp.poll();
                return new OrOpt();
            }
            return new BitOr();
        case '&':
            exp.poll();
            switch(exp.peek()){
            case '&':
                exp.poll();
                return new AndOpt();
            }
            return new BitAnd();
        case '~':
            exp.poll();
            return new BitNot();
        case '^':
            exp.poll();
            return new BitXro();
        case '?':
            exp.poll();
            return new QuestionOpt();
        case ':':
            exp.poll();
            return new QuestionSelectOpt();
        
        case '.':
        	char p = exp.peek(1);
        	if (p != '\'' && p != '"' && !Character.isJavaIdentifierStart(p)){
                return nullobj;
            }
            exp.poll();
            return new AccessOpt();
        case ',':
            exp.poll();
            return new CommaOpt();
        case '[':
            exp.poll();
            return new Object[]{new ArrayOpt(),new LBracketOpt()};
        case ']':
            exp.poll();
            return new Object[]{new RBracketOpt(), new FetchArrayOpt()};
        }
        return nullobj;
    }

}
