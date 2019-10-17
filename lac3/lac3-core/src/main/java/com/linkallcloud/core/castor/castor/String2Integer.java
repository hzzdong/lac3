package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.lang.Nums;

public class String2Integer extends String2Number<Integer> {

    @Override
    protected Integer getPrimitiveDefaultValue() {
        return 0;
    }

    @Override
    protected Integer valueOf(String str) {
        Nums.Radix ni = Nums.evalRadix(str);
        return Integer.valueOf(ni.val, ni.radix);
    }

}
