package com.linkallcloud.core.castor.castor;

import java.util.Map;

import com.linkallcloud.core.castor.Castor;

@SuppressWarnings("rawtypes")
public class Map2Boolean extends Castor<Map, Boolean> {

    @Override
    public Boolean cast(Map src, Class<?> toType, String... args) {
        if (null == src)
            return Boolean.FALSE;

        return true;
    }

}