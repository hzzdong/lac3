package com.linkallcloud.core.castor.castor;

import java.io.File;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

public class File2String extends Castor<File, String> {

    @Override
    public String cast(File src, Class<?> toType, String... args) throws FailToCastObjectException {
        return src.getAbsolutePath();
    }

}
