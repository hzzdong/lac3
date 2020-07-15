package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class LessEqual extends CompareRule {
	private static final long serialVersionUID = 5885755045865201776L;

	public LessEqual() {
		super();
	}

	/**
	 * @param rd
	 */
	public LessEqual(IRuleDescriptor rd) {
		super(rd);
	}

	/**
	 * @param field
	 * @param data
	 */
	public LessEqual(String field, Object data) {
		super(field, Operator.le, data);
	}

	/**
	 * @param field
	 * @param data
	 * @param fieldType
	 */
	public LessEqual(String field, String data, Class<?> fieldType) {
		super(field, Operator.le, data, fieldType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.QueryRule#getOperater()
	 */
	@Override
	public String getOperater() {
		return "<=";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected boolean compare(Object destValue) {
		if (destValue instanceof Comparable && this.getValue() instanceof Comparable) {
			return ((Comparable) destValue).compareTo((Comparable) this.getValue()) <= 0;
		}
		return false;
	}
}
