package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Enum2String extends Castor<Enum, String> {

    @Override
    public String cast(Enum src, Class<?> toType, String... args) throws FailToCastObjectException {
        return src.name();
    }
}
