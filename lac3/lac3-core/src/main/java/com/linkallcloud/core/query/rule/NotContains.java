package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class NotContains extends Contains {
    private static final long serialVersionUID = 1325364119588031926L;

    /**
     *
     */
    public NotContains() {
        super();
    }

    /**
     * @param rd
     */
    public NotContains(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public NotContains(String field, Object data) {
        super(field, data);
        setOp(Operator.nc);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public NotContains(String field, String data, Class<?> fieldType) {
        super(field, data, fieldType);
        setOp(Operator.nc);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.Contains#getOperater()
     */
    @Override
    public String getOperater() {
        return " NOT LIKE ";
    }

    @Override
    protected boolean compare(Object destValue) {
        return ((String) destValue).indexOf((String) this.getValue()) == -1;
    }
}
