package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.meta.Email;

public class String2Email extends Castor<String, Email> {

    @Override
    public Email cast(String src, Class<?> toType, String... args) {
        return new Email(src);
    }

}
