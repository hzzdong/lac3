package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class String2Enum extends Castor<String, Enum> {

    @SuppressWarnings("unchecked")
    @Override
    public Enum cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
        try {
            return Enum.valueOf((Class<Enum>) toType, src);
        }
        catch (IllegalArgumentException e) {
            for (Object c : toType.getEnumConstants()) {
                if (c.toString().equals(src)) return (Enum) c;
            }

            throw e;
        }
    }

}
