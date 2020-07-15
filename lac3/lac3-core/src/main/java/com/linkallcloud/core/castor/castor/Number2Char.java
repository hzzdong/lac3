package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Number2Char extends Castor<Number, Character> {

    @Override
    public Character cast(Number src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return (char) src.intValue();
    }

}
