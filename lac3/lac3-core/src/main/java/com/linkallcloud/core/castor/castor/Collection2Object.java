package com.linkallcloud.core.castor.castor;

import java.util.Collection;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"rawtypes"})
public class Collection2Object extends Castor<Collection, Object> {

    @Override
    public Object cast(Collection src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (src.size() == 0)
            return null;
        return Castors.me().castTo(src.iterator().next(), toType);
    }

}
