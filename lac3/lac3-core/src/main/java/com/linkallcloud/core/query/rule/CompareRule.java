package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.PropertyType;
import com.linkallcloud.core.query.QueryHelper;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;
import com.linkallcloud.core.query.rule.desc.ObjectRuleDescriptor;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.core.util.IConstants;

public abstract class CompareRule extends QueryRule {
    private static final long serialVersionUID = 6324001734070652009L;

    private Object value;

    public CompareRule() {
        super();
    }

    /**
     * 默认构造第一个Rule
     * 
     * @param rd
     */
    public CompareRule(IRuleDescriptor rd) {
        super(rd);
        setValueFromRuleDescriptor(rd);
    }

    /**
     * 从IRuleDescriptor中解析出data并转换成正确的类型赋值给this.value
     * 
     * @param rd
     */
    protected void setValueFromRuleDescriptor(IRuleDescriptor rd) {
        if (rd instanceof ObjectRuleDescriptor) {
            this.value = ((ObjectRuleDescriptor) rd).getData();
        } else if (rd instanceof StringRuleDescriptor) {
            String typeName = (rd.getTypes() != null && rd.getTypes().length > 0) ? rd.getTypes()[0] : "S";
            Class<?> typeClass = null;
            try {
                typeClass = Enum.valueOf(PropertyType.class, typeName).getValue();
            } catch (Throwable e) {
                try {
                    typeClass = Class.forName(typeName);
                } catch (ClassNotFoundException e1) {
                    throw new IllegalArgumentException("RuleDescriptor中type不支持：" + typeName, e);
                }
            }
            this.value = Castors.me().castTo(((StringRuleDescriptor) rd).getData(), typeClass);
        }
    }

    /**
     * @param field
     * @param op
     */
    public CompareRule(String field, Operator op, Object data) {
        super(field, op);
        this.value = data;
    }

    /**
     * 
     * @param field
     * @param op
     * @param data
     * @param fieldType
     */
    public CompareRule(String field, Operator op, Object data, Class<?> fieldType) {
        super(field, op);
        this.value = Castors.me().castTo(data, fieldType);
    }

    /**
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 解析目标值（destValue）是否满足本QueryRule
     *
     * @param destValue
     * @return
     */
    @Override
    public boolean parse(Object destValue) {
        if (this.getValue().getClass().equals(destValue.getClass())) {
            return compare(destValue);
        } else {
            Object v = Castors.me().castTo(destValue, this.getValue().getClass());
            return compare(v);
        }
    }

    protected abstract boolean compare(Object destValue);

    /*
     * 输出sql的Where对象:sql and args
     * 
     * @param where
     * 
     * @param jdbcMappingField 若 true 按照JDBC默认规则简单处理， false 不处理
     * 
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.QueryExpression#toWhere(com.linkallcloud.dao.query.Where, boolean)
     */
    @Override
    public void toWhere(QueryHelper where, boolean jdbcMappingField) {
        String aliasFieldName = getAliasFieldName(jdbcMappingField);
        // if (!aliasFieldName.startsWith(DEFAULT_ALIAS_NAME)) {
        // aliasFieldName = DEFAULT_ALIAS_NAME + aliasFieldName;
        // }
        appendSimpleRule(where, aliasFieldName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.QueryExpression#toWhere4Hql(com.linkallcloud.dao.query.Where)
     */
    @Override
    public void toWhere4Hql(QueryHelper where) {
        toWhere(where, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.QueryRule#appendSimpleRule(com.linkallcloud.dao.query.Where,
     * java.lang.String)
     */
    protected void appendSimpleRule(QueryHelper where, String aliasFieldName) {
        // field
        where.appendSql(aliasFieldName);
        // op
        where.appendSql(getOperater()).appendSql(getArgFlag());

        // value
        Object arg = getCompareValue();
        if (arg != null) {
            where.appendArg(arg);
        }
    }

    /**
     * @return
     */
    protected String getArgFlag() {
        return String.valueOf(IConstants.QUESTION_MARK);
    }

    /**
     * 根据比较类型，修改字段的值
     */
    public Object getCompareValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + " " + Castors.me().castTo(this.value, String.class);
    }

}
