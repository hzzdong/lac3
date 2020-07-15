package com.linkallcloud.core.lang.born;

import java.lang.reflect.Method;

import com.linkallcloud.core.lang.Mirror;

public class DynaMethodBorning<T> implements Borning<T> {

    private Method method;

    public DynaMethodBorning(Method method) {
        this.method = method;
        if (!method.isAccessible())
            this.method.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    public T born(Object... args) {
        try {
            return (T) method.invoke(null, Mirror.evalArgToRealArray(args));
        }
        catch (Exception e) {
            throw new BorningException(e, method.getDeclaringClass(), args);
        }
    }

}
