package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Collection2Array extends Castor<Collection, Object> {

    public Collection2Array() {
        this.fromClass = Collection.class;
        this.toClass = Array.class;
    }

    @Override
    public Object cast(Collection src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        Class<?> compType = toType.getComponentType();
        Object ary = Array.newInstance(compType, src.size());
        int index = 0;
        for (Iterator it = src.iterator(); it.hasNext();) {
            Array.set(ary, index++, Castors.me().castTo(it.next(), compType));
        }
        return ary;
    }

}
