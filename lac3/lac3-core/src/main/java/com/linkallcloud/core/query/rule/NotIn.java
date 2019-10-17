package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class NotIn extends In {
	private static final long serialVersionUID = -6503191508846443421L;

	public NotIn() {
		super();
	}

	/**
	 * @param rd
	 */
	public NotIn(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param op
	 * @param data
	 */
	public NotIn(String field, Object data) {
		super(field, data);
		setOp(Operator.ni);
	}

	/**
	 * @param field
	 * @param data
	 * @param fieldType
	 */
	// public NotIn(String field, String data, Class<?> fieldType) {
	// super(field, data, fieldType);
	// setOp(Operator.ni);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return " NOT IN ";
	}

}
