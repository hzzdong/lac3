package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public class IsNotNull extends IsNull {
    private static final long serialVersionUID = -3871870017387319334L;

    public IsNotNull() {
        super();
    }

    /**
     * @param rd
     */
    public IsNotNull(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     */
    public IsNotNull(String field) {
        super(field);
        setOp(Operator.nn);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return " IS NOT NULL ";
    }

    @Override
    public boolean parse(Object destValue) {
        return destValue != null;
    }
}
