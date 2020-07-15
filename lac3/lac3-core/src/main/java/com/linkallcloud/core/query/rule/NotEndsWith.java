package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class NotEndsWith extends EndsWith {
	private static final long serialVersionUID = 3415598221053269922L;

	/**
	 * 
	 */
	public NotEndsWith() {
		super();
	}

	/**
	 * @param rd
	 */
	public NotEndsWith(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public NotEndsWith(String field, Object data) {
		super(field, data);
		setOp(Operator.en);
	}

	/**
	 * @param field
	 * @param data
	 * @param fieldType
	 */
	public NotEndsWith(String field, String data, Class<?> fieldType) {
		super(field, data, fieldType);
		setOp(Operator.en);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return " NOT LIKE ";
	}

	@Override
	protected boolean compare(Object destValue) {
		if (destValue != null) {
			int len = ((String) destValue).length() - ((String) this.getValue()).length();
			int idx = ((String) destValue).indexOf((String) this.getValue());
			if (idx != -1 && idx == len) {
				return false;
			}
		}
		return true;
	}
}
