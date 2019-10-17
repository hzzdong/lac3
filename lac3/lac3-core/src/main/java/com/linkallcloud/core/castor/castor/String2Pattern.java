package com.linkallcloud.core.castor.castor;

import java.util.regex.Pattern;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class String2Pattern extends Castor<String, Pattern> {

    @Override
    public Pattern cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        try {
            return Pattern.compile(src);
        }
        catch (Exception e) {
            throw new FailToCastObjectException("Error regex: " + src, e);
        }
    }

}
