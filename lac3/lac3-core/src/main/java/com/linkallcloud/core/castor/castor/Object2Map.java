package com.linkallcloud.core.castor.castor;

import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Object2Map extends Castor<Object, Map> {

    @SuppressWarnings("unchecked")
    @Override
    public Map cast(Object src, Class<?> toType, String... args) throws FailToCastObjectException {
        return Lang.obj2map(src, (Class<? extends Map>) ((Class<? extends Map>) toType));
    }

}
