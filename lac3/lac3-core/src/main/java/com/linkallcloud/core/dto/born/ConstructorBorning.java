package com.linkallcloud.core.dto.born;

import java.lang.reflect.Constructor;

/**
 * 参数完全匹配的构造器构造工厂
 * 
 */
public class ConstructorBorning implements DtoBorning {
	private Constructor<?> c;

	public ConstructorBorning(Constructor<?> c) {
		this.c = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zj.pubinfo.dto.born.DtoBorning#born(java.lang.Object[])
	 */
	public Object born(Object[] args) throws Exception {
		return c.newInstance(args);
	}

}
