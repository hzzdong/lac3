package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class IsNull extends ExpRule {
	private static final long serialVersionUID = 5901463585100887593L;

	public IsNull() {
		super();
	}

	/**
	 * @param rd
	 */
	public IsNull(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 */
	public IsNull(String field) {
		super(field, Operator.nu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return " IS NULL ";
	}

}
