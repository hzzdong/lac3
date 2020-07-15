package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Number2Character extends Castor<Number, Character> {

    @Override
    public Character cast(Number src, Class<?> toType, String... args) {
        return (char) src.intValue();
    }

}
