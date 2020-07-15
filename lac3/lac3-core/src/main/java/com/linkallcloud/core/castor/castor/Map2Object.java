package com.linkallcloud.core.castor.castor;

import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Map2Object extends Castor<Map, Object> {

    @Override
    public Object cast(Map src, Class<?> toType, String... args) throws FailToCastObjectException {
        return Lang.map2Object(src, toType);
    }

}
