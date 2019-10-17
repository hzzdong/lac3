package com.linkallcloud.core.castor.castor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.linkallcloud.core.castor.Castor;

public class String2DateFormat extends Castor<String, DateFormat> {

    public DateFormat cast(String src, Class<?> toType, String... args){
        return new SimpleDateFormat(src);
    }

}
