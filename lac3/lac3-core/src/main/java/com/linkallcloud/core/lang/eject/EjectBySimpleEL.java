package com.linkallcloud.core.lang.eject;

import java.lang.reflect.Method;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;

public class EjectBySimpleEL implements Ejecting {

    private String by;

    private Method method;

    public EjectBySimpleEL(String by) {
        if (Strings.isBlank(by))
            throw new IllegalArgumentException("MUST NOT Null/Blank");
        if (by.indexOf('#') > 0) {
            try {
                method = Lang.loadClass(by.substring(0, by.indexOf('#')))
                                .getMethod(by.substring(by.indexOf('#')+1), Object.class);
            }
            catch (Throwable e) {
                throw Lang.wrapThrow(e);
            }
        }
        this.by = by;
    }

    public Object eject(Object obj) {
        try {
            if (method != null)
                return method.invoke(null, obj);
            if (obj == null)
                return null;
            return obj.getClass().getMethod(by).invoke(obj);
        }
        catch (Throwable e) {
            throw Lang.wrapThrow(e);
        }
    }

}
