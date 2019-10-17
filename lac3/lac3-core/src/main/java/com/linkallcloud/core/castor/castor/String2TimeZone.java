package com.linkallcloud.core.castor.castor;

import java.util.TimeZone;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.Strings;

public class String2TimeZone extends Castor<String, TimeZone> {

    @Override
    public TimeZone cast(String src, Class<?> toType, String... args) {
        if (Strings.isBlank(src))
            return null;
        return TimeZone.getTimeZone(src);
    }

}
