package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.lang.Nums;

public class String2Short extends String2Number<Short> {

    @Override
    protected Short getPrimitiveDefaultValue() {
        return 0;
    }

    @Override
    protected Short valueOf(String str) {
        Nums.Radix ni = Nums.evalRadix(str);
        return Short.valueOf(ni.val, ni.radix);
    }

}
