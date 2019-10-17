package com.linkallcloud.core.castor.castor;

import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings("rawtypes")
public class Map2Enum extends Castor<Map, Enum> {

    @Override
    public Enum cast(Map src, Class<?> toType, String... args) throws FailToCastObjectException {
        return null;
    }

}
