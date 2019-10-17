package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.lang.Strings;

public class String2Datetime extends DateTimeCastor<String, java.util.Date> {

    @Override
    public java.util.Date cast(String src, Class<?> toType, String... args) {
        // 处理空白
        if (Strings.isBlank(src))
            return null;
        return toDate(src);
    }

}
