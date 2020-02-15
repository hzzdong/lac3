package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class BeginsWith extends CompareRule {
    private static final long serialVersionUID = -7209328941816556036L;

    public BeginsWith() {
        super();
    }

    /**
     * @param rd
     */
    public BeginsWith(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public BeginsWith(String field, Object data) {
        super(field, Operator.bw, data);
    }

    /**
     * @param field
     * @param data
     * @param fieldType
     */
    public BeginsWith(String field, String data, Class<?> fieldType) {
        super(field, Operator.bw, data, fieldType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return " LIKE ";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.rule.CompareRule#getCompareValue()
     */
    @Override
    public Object getCompareValue() {
        return getValue() + "%";
    }

    @Override
    protected boolean compare(Object destValue) {
        if (destValue != null) {
            return ((String) destValue).indexOf((String) this.getValue()) == 0;
        }
        return false;
    }

}
