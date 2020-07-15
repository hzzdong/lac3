package com.linkallcloud.core.castor.castor;

import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Datetime2SqlDate extends Castor<Date, java.sql.Date> {

    @Override
    public java.sql.Date cast(Date src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return new java.sql.Date(src.getTime());
    }

}
