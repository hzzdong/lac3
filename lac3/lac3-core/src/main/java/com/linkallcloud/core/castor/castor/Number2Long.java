package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Long extends Castor<Number, Long> {

    @Override
    public Long cast(Number src, Class<?> toType, String... args) {
        return src.longValue();
    }

}
