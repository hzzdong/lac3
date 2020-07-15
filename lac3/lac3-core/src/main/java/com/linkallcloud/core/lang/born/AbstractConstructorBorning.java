package com.linkallcloud.core.lang.born;

import java.lang.reflect.Constructor;

import com.linkallcloud.core.conf.RcConf;
import com.linkallcloud.core.lang.reflect.FastClassFactory;
import com.linkallcloud.core.lang.reflect.FastMethod;

public abstract class AbstractConstructorBorning {

    protected Constructor<?> c;
    protected FastMethod fm;
    
    public AbstractConstructorBorning(Constructor<?> c) {
        super();
        if (!c.isAccessible())
            c.setAccessible(true);
        this.c = c;
    }
    
    protected Object call(Object...args) throws Exception {
        if (RcConf.USE_FASTCLASS) {
            if (fm == null)
                fm = FastClassFactory.get(c);
            return fm.invoke(null, args);
        }
        return c.newInstance(args);
    }
}
