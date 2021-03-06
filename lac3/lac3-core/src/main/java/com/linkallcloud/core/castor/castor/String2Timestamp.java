package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;

import com.linkallcloud.core.lang.Strings;

public class String2Timestamp extends DateTimeCastor<String, Timestamp> {

    @Override
    public Timestamp cast(String src, Class<?> toType, String... args) {
        if (Strings.isBlank(src))
            return null;

        return new java.sql.Timestamp(toDate(src).getTime());

    }

}
