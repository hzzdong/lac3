package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;
import java.util.Calendar;

import com.linkallcloud.core.castor.Castor;

public class Calendar2Timestamp extends Castor<Calendar, Timestamp> {

    @Override
    public Timestamp cast(Calendar src, Class<?> toType, String... args) {
        long ms = src.getTimeInMillis();
        return new Timestamp(ms);
    }
}
