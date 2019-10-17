package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class Less extends CompareRule {
	private static final long serialVersionUID = -8018833469538117189L;

	public Less() {
		super();
	}

	/**
	 * @param rd
	 */
	public Less(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public Less(String field, Object data) {
		super(field, Operator.lt, data);
	}

	/**
	 * @param field
	 * @param data
	 * @param fieldType
	 */
	public Less(String field, String data, Class<?> fieldType) {
		super(field, Operator.lt, data, fieldType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return "<";
	}

}
