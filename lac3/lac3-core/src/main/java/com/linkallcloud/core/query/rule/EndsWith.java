package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class EndsWith extends CompareRule {
	private static final long serialVersionUID = -698898745137146620L;

	public EndsWith() {
		super();
	}

	/**
	 * @param rd
	 */
	public EndsWith(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public EndsWith(String field, Object data) {
		super(field, Operator.ew, data);
	}

	/**
	 * @param field
	 * @param op
	 * @param data
	 * @param fieldType
	 */
	public EndsWith(String field, String data, Class<?> fieldType) {
		super(field, Operator.ew, data, fieldType);
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
		return "%" + getValue();
	}

}
