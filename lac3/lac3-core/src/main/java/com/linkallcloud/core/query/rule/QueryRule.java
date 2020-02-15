package com.linkallcloud.core.query.rule;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Expression;
import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.QueryHelper;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;

import java.util.ArrayList;
import java.util.List;

public abstract class QueryRule implements Expression {
    private static final long serialVersionUID = -4551477168517876211L;

    public static final String DEFAULT_ALIAS_NAME = "o";

    private String field;
    private Operator op;

    public QueryRule() {
        super();
    }

    public QueryRule(IRuleDescriptor rd) {
        this.field = rd.getFields()[0];
        this.setOp(op);
    }

    /**
     * @param field
     * @param op
     */
    public QueryRule(String field, Operator op) {
        super();
        this.field = field;
        this.op = op;
    }

    /**
     * @param field
     * @param op
     */
    public QueryRule(String field, String op) {
        super();
        this.field = field;
        try {
            this.op = Enum.valueOf(Operator.class, op);
        } catch (Throwable e) {
            throw new IllegalArgumentException("op(" + op + ") not support.", e);
        }
    }

    @Override
    public String toString() {
        return this.field + " " + this.op.name();
    }

    /**
     * 得到操作符
     */
    public abstract String getOperater();

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the op
     */
    public Operator getOp() {
        return op;
    }

    /**
     * @param op the op to set
     */
    public void setOp(Operator op) {
        this.op = op;
    }

    public void setOp(String op) {
        try {
            this.op = Enum.valueOf(Operator.class, op);
        } catch (Throwable e) {
            throw new IllegalArgumentException("op(" + op + ") not support.", e);
        }
    }

    /**
     * 解析目标值（destValue）是否满足本QueryRule
     *
     * @param destValue
     * @return
     */
    public abstract boolean parse(Object destValue);

    /**
     * 根据提供的aliasFieldName，往where对象中append sql，args。
     *
     * @param where
     * @param aliasFieldName
     */
    protected abstract void appendSimpleRule(QueryHelper where, String aliasFieldName);

    /**
     * 根据字段中信息，返回字段名称
     *
     * @param jdbcMappingField 若 true 按照JDBC默认规则简单处理， false 不处理
     */
    protected String getAliasFieldName(boolean jdbcMappingField) {
        String aliasFieldName = this.field.replaceAll("#", "\\.");
        if (jdbcMappingField) {
            return Strings.splitWordsWithUnderline(aliasFieldName);
        } else {
            return aliasFieldName;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryExpression#toSmartHqlQuery(com.linkallcloud. dao.query.Where)
     */
    @Override
    public void toSmartHqlQuery(QueryHelper where) {
        String aliasFieldName = getAliasFieldName(false);
        int dollorIndex = aliasFieldName.lastIndexOf("$");
        if (dollorIndex == -1) {// 不需要连接:area.code
            String parentAlias = DEFAULT_ALIAS_NAME;
            appendSimpleRule(where, parentAlias + "." + aliasFieldName);
        } else if (dollorIndex == 0) {// $pets.name
            String parentAlias = DEFAULT_ALIAS_NAME;

            int dotIndex = aliasFieldName.indexOf(".");
            String field = aliasFieldName.substring(1, dotIndex == -1 ? aliasFieldName.length() : dotIndex);// pets
            String alias = "_" + field.toLowerCase();
            String newAlias = where.checkUniqueAlias(alias, field, parentAlias);// _pets (or others)

            if (dotIndex == -1) {// $pets 应该没有这样的查询条件吧?自动加上.id->$pets.id
                appendSimpleRule(where, newAlias + ".id");// _pets.id
            } else {// $pets.name
                appendSimpleRule(where, newAlias + aliasFieldName.substring(dotIndex));// _pets.name
            }
        } else {// pet.area$shops.name
            String joinAlias = aliasFieldName.substring(0, dollorIndex);// pet.area
            int length = joinAlias.length();
            List<String> aliases = new ArrayList<String>();
            int start = 0;
            for (int i = 0; i < length; i++) {
                if (joinAlias.charAt(i) == '$' || joinAlias.charAt(i) == '.') {
                    if (i != 0) {
                        aliases.add(joinAlias.substring(start, i));
                        start = i + 1;
                    } else {
                        start = 1;
                    }
                }
            }
            aliases.add(joinAlias.substring(start, length));// alias:[pet,area]

            String parentAlias = DEFAULT_ALIAS_NAME;
            if (aliases.size() > 0) {
                for (String field : aliases) {
                    String alias = "_" + field.toLowerCase();
                    String newAlias = where.checkUniqueAlias(alias, field, parentAlias);
                    parentAlias = newAlias;
                }
            }

            String joinAliaField = aliasFieldName.substring(dollorIndex + 1);// shops.name
            int dotIndex = joinAliaField.indexOf(".");
            String field = (dotIndex == -1) ? joinAliaField : joinAliaField.substring(0, dotIndex);// shops; shops.name
            // -> shops
            String alias = "_" + field.toLowerCase();
            String newAlias = where.checkUniqueAlias(alias, field, parentAlias);// _shops (or others)
            if (dotIndex == -1) {// shops应该没有这样的查询条件吧?自动加上.id->shops.id
                appendSimpleRule(where, newAlias + ".id");// _shops.id
            } else {// $pets.name
                appendSimpleRule(where, newAlias + joinAliaField.substring(dotIndex));// _shops.name
            }
        }

    }

}
