package com.linkallcloud.core.lang.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class PType<T> extends RcType {
    
        public PType() {
            Type t = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            ParameterizedType pt = (ParameterizedType)t;
            setActualTypeArguments(pt.getActualTypeArguments());
            setRawType(pt.getRawType());
            setOwnerType(pt.getOwnerType());
        }
    }