package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Double extends Castor<Number, Double> {

    @Override
    public Double cast(Number src, Class<?> toType, String... args) {
        return src.doubleValue();
    }

}
