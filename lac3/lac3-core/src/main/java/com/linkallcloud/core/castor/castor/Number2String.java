package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2String extends Castor<Number, String> {

    @Override
    public String cast(Number src, Class<?> toType, String... args) {
        return src.toString();
    }

}
