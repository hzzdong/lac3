package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Times;

public abstract class DateTimeCastor<FROM, TO> extends Castor<FROM, TO> {

    protected java.util.Date toDate(String src) {
        try {
            return Times.D(src);
        }
        catch (Throwable e) {
            throw new FailToCastObjectException(e,
                                                "'%s' to %s",
                                                src,
                                                java.util.Date.class.getName());
        }
    }

}
