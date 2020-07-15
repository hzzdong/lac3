package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class String2Character extends Castor<String, Character> {

    @Override
    public Character cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return src.charAt(0);
    }

}
