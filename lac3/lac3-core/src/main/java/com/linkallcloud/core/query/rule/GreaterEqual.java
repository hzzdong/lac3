package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class GreaterEqual extends CompareRule {
    private static final long serialVersionUID = -1154939728473336622L;

    public GreaterEqual() {
        super();
    }

    /**
     * @param rd
     */
    public GreaterEqual(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public GreaterEqual(String field, Object data) {
        super(field, Operator.ge, data);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public GreaterEqual(String field, String data, Class<?> fieldType) {
        super(field, Operator.ge, data, fieldType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return ">=";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected boolean compare(Object destValue) {
        if (destValue instanceof Comparable && this.getValue() instanceof Comparable) {
            return ((Comparable) destValue).compareTo((Comparable) this.getValue()) >= 0;
        }
        return false;
    }

}
