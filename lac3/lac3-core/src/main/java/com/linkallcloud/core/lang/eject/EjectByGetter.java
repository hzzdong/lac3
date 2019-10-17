package com.linkallcloud.core.lang.eject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.linkallcloud.core.conf.RcConf;
import com.linkallcloud.core.lang.FailToGetValueException;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.lang.reflect.FastClassFactory;
import com.linkallcloud.core.lang.reflect.FastMethod;

public class EjectByGetter implements Ejecting {

    private static final Log log = Logs.get();

    private Method getter;
    
    protected FastMethod fm;

    public EjectByGetter(Method getter) {
        this.getter = getter;
    }

    public Object eject(Object obj) {
        try {
            if (obj == null)
                return null;
            if (RcConf.USE_FASTCLASS) {
                if (fm == null)
                    fm = FastClassFactory.get(getter);
                return fm.invoke(obj);
            }
            return getter.invoke(obj);
        }
        catch (InvocationTargetException e) {
            throw new FailToGetValueException("getter=" + getter, e);
        }
        catch (Exception e) {
            if (log.isInfoEnabled())
                log.info("Fail to value by getter", e);
            throw Lang.makeThrow(    "Fail to invoke getter %s.'%s()' %s because [%s]: %s",
                                    getter.getDeclaringClass().getName(),
                                    getter.getName(),
                                    (obj == null || getClass().getDeclaringClass() == obj.getClass() ? "" : "<"+obj.getClass()+">"),
                                    Lang.unwrapThrow(e),
                                    Lang.unwrapThrow(e).getMessage());
        }
    }

}
