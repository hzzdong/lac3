package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class Equal extends CompareRule {
	private static final long serialVersionUID = -8596128806725259019L;

	public Equal() {
		super();
	}

	/**
	 * @param rd
	 */
	public Equal(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public Equal(String field, Object data) {
		super(field, Operator.eq, data);
	}

	/**
	 * @param field
	 * @param op
	 * @param data
	 * @param fieldType
	 */
	public Equal(String field, String data, Class<?> fieldType) {
		super(field, Operator.eq, data, fieldType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return "=";
	}

}
