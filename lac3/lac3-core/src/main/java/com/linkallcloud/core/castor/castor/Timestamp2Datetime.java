package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;
import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Timestamp2Datetime extends Castor<Timestamp, Date> {

    @Override
    public Date cast(Timestamp src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return new java.util.Date(src.getTime());
    }

}
