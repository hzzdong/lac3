package com.linkallcloud.core.castor.castor;

import java.util.Set;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;

@SuppressWarnings("rawtypes")
public class String2Set extends Castor<String, Set> {

    @Override
    public Set cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
        return Json.fromJson(Set.class, src);
    }

}