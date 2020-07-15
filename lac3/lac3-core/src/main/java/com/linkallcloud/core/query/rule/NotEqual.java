package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class NotEqual extends CompareRule {
    private static final long serialVersionUID = -4132209425654319411L;

    public NotEqual() {
        super();
    }

    /**
     * @param rd
     */
    public NotEqual(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public NotEqual(String field, Object data) {
        super(field, Operator.ne, data);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public NotEqual(String field, String data, Class<?> fieldType) {
        super(field, Operator.ne, data, fieldType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return "<>";
    }

    @Override
    protected boolean compare(Object destValue) {
        return !this.getValue().equals(destValue);
    }
}
