package com.linkallcloud.core.castor.castor;

import java.sql.Timestamp;
import java.util.Date;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class Datetime2Timpestamp extends Castor<Date, Timestamp> {

    @Override
    public Timestamp cast(Date src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return new Timestamp(src.getTime());
    }

}
