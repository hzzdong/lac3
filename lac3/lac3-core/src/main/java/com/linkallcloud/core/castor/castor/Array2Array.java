package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Array;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Lang;

public class Array2Array extends Castor<Object, Object> {

    public Array2Array() {
        this.fromClass = Array.class;
        this.toClass = Array.class;
    }

    @Override
    public Object cast(Object src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return Lang.array2array(src, toType.getComponentType());
    }

}
