package com.linkallcloud.core.castor.castor;

import java.util.Calendar;

import com.linkallcloud.core.lang.Times;

public class Calendar2String extends DateTimeCastor<Calendar, String> {

    @Override
    public String cast(Calendar src, Class<?> toType, String... args) {
        return Times.sDT(src.getTime());
    }

}
