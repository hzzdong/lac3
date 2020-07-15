package com.linkallcloud.core.castor.castor;

import java.sql.Time;
import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Datetime2SqlTime extends Castor<Date, Time> {

    @Override
    public Time cast(Date src, Class<?> toType, String... args) throws FailToCastObjectException {
        return new Time(src.getTime());
    }

}
