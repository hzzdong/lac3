package com.linkallcloud.core.query;

import java.math.BigDecimal;
import java.util.Date;

public enum PropertyType {
    S(String.class), I(Integer.class), L(Long.class), N(Double.class),

    D(Date.class)/* yyyy-MM-dd */, T(Date.class)/* yyyy-MM-dd HH:mm:ss */,

    B(Boolean.class), O(Object.class), Q(BigDecimal.class);

    private Class<?> clazz;

    PropertyType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getValue() {
        return clazz;
    }
}
