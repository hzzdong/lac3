package com.linkallcloud.core.castor.castor;

import java.util.Collection;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class String2Collection extends Castor<String, Collection> {

    @Override
    public Collection cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return (Collection) Json.fromJson(toType, Lang.inr(src));
    }

}
