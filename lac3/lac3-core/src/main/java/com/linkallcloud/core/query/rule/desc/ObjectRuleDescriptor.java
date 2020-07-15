package com.linkallcloud.core.query.rule.desc;

import com.linkallcloud.core.query.Operator;

public class ObjectRuleDescriptor extends RuleDescriptor {

	private Operator op;// 操作
	private Object data;

	public ObjectRuleDescriptor() {
		super();
	}

	public ObjectRuleDescriptor(String field, Operator op, Object data) {
		super(field, null);
		this.op = op;
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getOperator()
	 */
	public Operator getOperator() {
		return op;
	}

	/**
	 * @return the op
	 */
	public Operator getOp() {
		return op;
	}

	/**
	 * @param op
	 *            the op to set
	 */
	public void setOp(Operator op) {
		this.op = op;
	}

	/**
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getValue()
	 */
	@Override
	public Object getValue() {
		return data;
	}

    /* (non-Javadoc)
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        this.data = value;
    }

}
