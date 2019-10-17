package com.linkallcloud.core.castor.castor;

import java.sql.Date;
import java.sql.Timestamp;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Timestamp2SqlDate extends Castor<Timestamp, Date> {

    @Override
    public Date cast(Timestamp src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return new Date(src.getTime());
    }

}
