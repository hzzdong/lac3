package com.linkallcloud.core.query.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.SqlFormator;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;
import com.linkallcloud.core.util.IConstants;

import java.lang.reflect.Array;
import java.util.List;

public class In extends CompareRule {
    private static final long serialVersionUID = -5796056338024742638L;

    public In() {
        super();
    }

    /**
     * @param rd
     */
    public In(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param data
     */
    public In(String field, Object data) {
        super(field, Operator.in, data);
    }

    // public In(String field, Object data, Class<?> fieldType) {
    // this(field, data);
    // }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.linkallcloud.dao.query.rule.CompareRule#setValueFromRuleDescriptor(com.
     * linkallcloud.dao.query.rule.desc. IRuleDescriptor )
     */
    @Override
    protected void setValueFromRuleDescriptor(IRuleDescriptor rd) {
        this.setValue(rd.getValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryRule#getOperater()
     */
    @Override
    public String getOperater() {
        return " IN ";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.rule.CompareRule#getArgFlag()
     */
    @Override
    protected String getArgFlag() {
        return parseInFieldValue(this.getValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.rule.CompareRule#getCompareValue()
     */
    @Override
    public Object getCompareValue() {
        return null;
    }

    /**
     * @param fieldValue
     * @return String
     */
    private String parseInFieldValue(Object fieldValue) {
        if (String.class.isAssignableFrom(fieldValue.getClass())) {
            String inStr = ((String) fieldValue).trim();
            if (Strings.isQuoteByIgnoreBlank(inStr, '[', ']')) {
                return "(" + inStr.substring(1, inStr.length() - 1) + ")";
            } else if (Strings.isQuoteByIgnoreBlank(inStr, '(', ')')) {
                return inStr;
            } else {
                return "('" + inStr + "')";
            }
        } else if (Array.class.isAssignableFrom(fieldValue.getClass())) {
            Object[] args = (Object[]) fieldValue;
            return SqlFormator.concatINSql(IConstants.COMMA, args).toString();
        } else if (List.class.isAssignableFrom(fieldValue.getClass())) {
            Object[] args = ((List<?>) fieldValue).toArray(new Object[((List<?>) fieldValue).size()]);
            return SqlFormator.concatINSql(IConstants.COMMA, args).toString();
        }
        throw new IllegalArgumentException("IN表达式中，data 只能是String,List或Array.");
    }

    @Override
    protected boolean compare(Object destValue) {
        String dest = Castors.me().castToString(destValue);
        List<Object> list = JSON.parseObject((String) this.getValue(), new TypeReference<List<Object>>() {
        });
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (dest.equals(obj.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
