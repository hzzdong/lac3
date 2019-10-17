package com.linkallcloud.core.castor.castor;

import java.util.Collection;
import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Lang;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Collection2Map extends Castor<Collection, Map> {

    @Override
    public Map cast(Collection src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (null == args || args.length == 0)
            throw Lang.makeThrow(    FailToCastObjectException.class,
                                    "For the elements in Collection %s, castors don't know which one is the key field.",
                                    src.getClass().getName());
        return Lang.collection2map((Class<Map<Object, Object>>) toType, src, args[0]);
    }

}
