package com.linkallcloud.core.castor.castor;

import java.util.Calendar;
import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Calendar2Datetime extends Castor<Calendar, Date> {

    @Override
    public Date cast(Calendar src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return src.getTime();
    }

}
