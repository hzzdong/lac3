package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Array;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Array2Object extends Castor<Object, Object> {

    public Array2Object() {
        this.fromClass = Array.class;
        this.toClass = Object.class;
    }

    @Override
    public Object cast(Object src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (Array.getLength(src) == 0)
            return null;
        return Castors.me().castTo(Array.get(src, 0), toType);
    }

}
