package com.linkallcloud.core.castor.castor;

import java.util.Calendar;

import com.linkallcloud.core.castor.Castor;

public class Calendar2Long extends Castor<Calendar, Long> {
    @Override
    public Long cast(Calendar src, Class<?> toType, String... args) {
        return src.getTimeInMillis();
    }
}
