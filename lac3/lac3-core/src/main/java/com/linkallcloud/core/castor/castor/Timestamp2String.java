package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;

import com.linkallcloud.core.lang.Times;

public class Timestamp2String extends DateTimeCastor<Timestamp, String> {

    @Override
    public String cast(Timestamp src, Class<?> toType, String... args) {
        return Times.sDT(Times.D(src.getTime()));
    }

}
