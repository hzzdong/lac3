package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.DateRange;

public class String2DateRange extends Castor<String, DateRange>{

	@Override
	public DateRange cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
		return new DateRange(src);
	}

}
