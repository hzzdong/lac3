package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Object2Class extends Castor<Object, Class> {

    @Override
    public Class cast(Object src, Class<?> toType, String... args) throws FailToCastObjectException {
        return src.getClass();
    }

}
