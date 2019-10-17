package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class Contains extends CompareRule {
	private static final long serialVersionUID = 6542092848078539869L;

	public Contains() {
		super();
	}

	/**
	 * @param rd
	 */
	public Contains(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public Contains(String field, Object data) {
		super(field, Operator.cn, data);
	}

	/**
	 * @param field
	 * @param data
	 * @param fieldType
	 */
	public Contains(String field, String data, Class<?> fieldType) {
		super(field, Operator.cn, data, fieldType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return " LIKE ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.rule.CompareRule#getCompareValue()
	 */
	@Override
	public Object getCompareValue() {
		return "%" + getValue() + "%";
	}

}
