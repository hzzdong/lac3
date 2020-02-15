package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.QueryHelper;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

public abstract class ExpRule extends QueryRule {
    private static final long serialVersionUID = 5806462863294410868L;

    public ExpRule() {
        super();
    }

    /**
     * @param rd
     */
    public ExpRule(IRuleDescriptor rd) {
        super(rd);
    }

    /**
     * @param field
     * @param op
     */
    public ExpRule(String field, Operator op) {
        super(field, op);
    }

    /*
     *
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
     * @see com.linkallcloud.dao.query.rule.QueryRule#appendSimpleRule(com.linkallcloud.dao.query.Where, java.lang.String)
     */
    protected void appendSimpleRule(QueryHelper where, String aliasFieldName) {
        // field
        where.appendSql(aliasFieldName);
        // op
        where.appendSql(getOperater());
        // value not need.
    }

}
