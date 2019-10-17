package com.linkallcloud.core.castor.castor;

import java.util.Collection;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.json.JsonFormat;

@SuppressWarnings({"rawtypes"})
public class Collection2String extends Castor<Collection, String> {

    @Override
    public String cast(Collection src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        return Json.toJson(src, JsonFormat.compact());
    }

}
