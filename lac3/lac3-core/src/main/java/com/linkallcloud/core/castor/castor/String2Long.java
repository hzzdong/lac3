package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.lang.Nums;

public class String2Long extends String2Number<Long> {

    @Override
    protected Long getPrimitiveDefaultValue() {
        return 0L;
    }

    @Override
    protected Long valueOf(String str) {
        Nums.Radix ni = Nums.evalRadix(str);
        return Long.valueOf(ni.val, ni.radix);
    }

}
