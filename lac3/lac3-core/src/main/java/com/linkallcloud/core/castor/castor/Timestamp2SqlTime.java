package com.linkallcloud.core.castor.castor;

import java.sql.Time;
import java.sql.Timestamp;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Timestamp2SqlTime extends Castor<Timestamp, Time> {

    @Override
    public Time cast(Timestamp src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return new Time(src.getTime());
    }

}
