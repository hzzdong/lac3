package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Object2Mirror extends Castor<Object, Mirror> {

    @Override
    public Mirror cast(Object src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return Mirror.me(src.getClass());
    }

}
