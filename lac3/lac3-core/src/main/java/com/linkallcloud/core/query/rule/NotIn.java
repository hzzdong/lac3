package com.linkallcloud.core.query.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

import java.util.List;

public class NotIn extends In {
    private static final long serialVersionUID = -6503191508846443421L;

    public NotIn() {
        super();
    }

    /**
     * @param rd
     */
    public NotIn(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public NotIn(String field, Object data) {
        super(field, data);
        setOp(Operator.ni);
    }

    // public NotIn(String field, String data, Class<?> fieldType) {
    // super(field, data, fieldType);
    // setOp(Operator.ni);
    // }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return " NOT IN ";
    }

    @Override
    protected boolean compare(Object destValue) {
        String dest = Castors.me().castToString(destValue);
        List<Object> list = JSON.parseObject((String) this.getValue(), new TypeReference<List<Object>>() {
        });
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (dest.equals(obj.toString())) {
                    return false;
                }
            }
        }
        return true;
    }
}
