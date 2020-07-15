package com.linkallcloud.core.castor.castor;

import java.util.Date;

import com.linkallcloud.core.castor.Castor;

public class Datetime2Long extends Castor<Date, Long> {

    @Override
    public Long cast(Date src, Class<?> toType, String... args) {
        return src.getTime();
    }

}
