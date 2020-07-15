package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;

import com.linkallcloud.core.castor.Castor;

public class Number2Timestamp extends Castor<Number, Timestamp> {

    @Override
    public Timestamp cast(Number src, Class<?> toType, String... args) {
        return new Timestamp(src.longValue());
    }

}
