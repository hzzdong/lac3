package com.linkallcloud.core.castor.castor;

import java.util.TimeZone;

import com.linkallcloud.core.castor.Castor;

public class TimeZone2String extends Castor<TimeZone, String> {

    @Override
    public String cast(TimeZone src, Class<?> toType, String... args) {
        return src.getID();
    }

}
