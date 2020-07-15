package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class NotBeginsWith extends BeginsWith {
    private static final long serialVersionUID = 2598695748834370994L;

    public NotBeginsWith() {
        super();
    }

    /**
     * @param rd
     */
    public NotBeginsWith(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public NotBeginsWith(String field, Object data) {
        super(field, data);
        setOp(Operator.bn);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public NotBeginsWith(String field, String data, Class<?> fieldType) {
        super(field, data, fieldType);
        setOp(Operator.bn);
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
            return ((String) destValue).indexOf((String) this.getValue()) != 0;
        }
        return true;
    }

}
