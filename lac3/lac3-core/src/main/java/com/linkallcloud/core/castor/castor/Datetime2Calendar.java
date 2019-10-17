package com.linkallcloud.core.castor.castor;

import java.util.Calendar;
import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Datetime2Calendar extends Castor<Date, Calendar> {

    @Override
    public Calendar cast(Date src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        return c;
    }

}
