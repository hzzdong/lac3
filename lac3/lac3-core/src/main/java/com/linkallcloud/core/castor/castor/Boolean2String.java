package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Boolean2String extends Castor<Boolean, String> {

    @Override
    public String cast(Boolean src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return String.valueOf(src);
    }

}
