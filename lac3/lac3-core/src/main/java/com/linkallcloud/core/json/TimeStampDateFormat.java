package com.linkallcloud.core.json;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.linkallcloud.core.lang.Lang;

/**
 * 仅用于将Date对象变成long
 *
 */
class TimeStampDateFormat extends SimpleDateFormat {

    private static final long serialVersionUID = 1L;

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return toAppendTo.append(""+date.getTime());
    }

    public Date parse(String source, ParsePosition pos) {
        throw Lang.noImplement();
    }

}
