package com.linkallcloud.core.castor.castor;

import java.io.File;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Files;

public class String2File extends Castor<String, File> {

    @Override
    public File cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
        return Files.findFile(src);
    }

}
