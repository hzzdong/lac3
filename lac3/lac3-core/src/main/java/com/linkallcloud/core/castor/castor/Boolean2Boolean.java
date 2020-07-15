package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Boolean2Boolean extends Castor<Boolean, Boolean> {

    @Override
    public Boolean cast(Boolean src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return src;
    }

}
