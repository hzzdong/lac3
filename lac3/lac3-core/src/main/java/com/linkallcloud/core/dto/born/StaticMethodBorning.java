package com.linkallcloud.core.dto.born;

import java.lang.reflect.Method;

/**
 * 构造参数完全匹配的静态方法构造工厂
 * 
 */
public class StaticMethodBorning implements DtoBorning {

	private Method method;

	/**
	 * 
	 */
	public StaticMethodBorning(Method method) {
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.zj.pubinfo.dto.born.DtoBorning#born(java.lang.Object[])
	 */
	public Object born(Object[] args) throws Exception {
		return method.invoke(null, args);
	}

}
