package com.linkallcloud.core.castor.castor;

import java.util.Calendar;

import com.linkallcloud.core.lang.Strings;

public class String2Calendar extends DateTimeCastor<String, Calendar> {

    @Override
    public Calendar cast(String src, Class<?> toType, String... args) {
        if (Strings.isBlank(src))
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(toDate(src));
        return c;
    }

}
