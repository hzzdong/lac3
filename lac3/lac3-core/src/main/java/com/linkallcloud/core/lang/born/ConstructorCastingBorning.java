package com.linkallcloud.core.lang.born;

import java.lang.reflect.Constructor;

import com.linkallcloud.core.lang.Lang;

public class ConstructorCastingBorning<T> extends AbstractConstructorBorning implements Borning<T> {

    private Class<?>[] pts;

    public ConstructorCastingBorning(Constructor<T> c) {
        super(c);
        this.pts = c.getParameterTypes();
    }

    @SuppressWarnings("unchecked")
    public T born(Object... args) {
        try {
            args = Lang.array2ObjectArray(args, pts);
            return (T) call(args);
        }
        catch (Exception e) {
            throw new BorningException(e, c.getDeclaringClass(), args);
        }
    }

}
