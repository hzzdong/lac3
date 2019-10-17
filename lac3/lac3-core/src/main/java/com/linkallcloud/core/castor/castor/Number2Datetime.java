package com.linkallcloud.core.castor.castor;

import java.util.Date;

import com.linkallcloud.core.castor.Castor;

public class Number2Datetime extends Castor<Number, Date> {

    @Override
    public Date cast(Number src, Class<?> toType, String... args) {
        return new java.util.Date(src.longValue());
    }

}
