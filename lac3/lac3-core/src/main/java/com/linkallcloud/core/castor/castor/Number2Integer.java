package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Integer extends Castor<Number, Integer> {

    @Override
    public Integer cast(Number src, Class<?> toType, String... args) {
        return src.intValue();
    }

}
