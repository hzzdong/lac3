package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Boolean extends Castor<Number, Boolean> {

    @Override
    public Boolean cast(Number src, Class<?> toType, String... args) {
        return src.intValue() == 0 ? false : true;
    }

}
