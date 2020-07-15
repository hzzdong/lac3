package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Short extends Castor<Number, Short> {

    @Override
    public Short cast(Number src, Class<?> toType, String... args) {
        return src.shortValue();
    }

}
