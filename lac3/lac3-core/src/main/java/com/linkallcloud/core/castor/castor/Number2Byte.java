package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Byte extends Castor<Number, Byte> {

    @Override
    public Byte cast(Number src, Class<?> toType, String... args) {
        return src.byteValue();
    }

}
