package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class Greater extends CompareRule {
    private static final long serialVersionUID = -7840727882698958008L;

    public Greater() {
        super();
    }

    /**
     * @param rd
     */
    public Greater(IRuleDescriptor rd) {
        super(rd);
    }

    @Override
    protected boolean compare(Object destValue) {
        if (destValue instanceof Comparable && this.getValue() instanceof Comparable) {
            return ((Comparable) destValue).compareTo((Comparable) this.getValue()) > 0;
        }
        return false;
    }

    /**
     * @param field
     * @param data
     */
    public Greater(String field, Object data) {
        super(field, Operator.gt, data);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public Greater(String field, String data, Class<?> fieldType) {
        super(field, Operator.gt, data, fieldType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return ">";
    }

}
