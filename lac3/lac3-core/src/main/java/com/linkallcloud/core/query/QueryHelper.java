package com.linkallcloud.core.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.util.IConstants;
import com.linkallcloud.core.util.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.linkallcloud.core.query.rule.QueryRule;

/**
 * Where条件对象。用于封装其它查询条件或者规则计算返回Where部分。
 * 
 */
public class QueryHelper {

    protected Log log = Logs.getLog(getClass());

    private StringBuffer sql;// where ：sql or hsql
    private List<Object> args;

    // alias,join sql
    private Map<String, String> joins;

    // 以下字段用于暂存完整的查询
    protected String query;
    protected String countQuery;
    protected String aggregateQuery;

    public QueryHelper() {
        this.sql = new StringBuffer();
        this.args = new ArrayList<Object>();
        this.joins = new LinkedHashMap<String, String>();
    }

    /**
     * 得到带占位符的where sql条件语句
     * 
     * @return cnd sql
     */
    public String getConditionString() {
        return SqlFormator.getConditionString(getSql());
    }

    /**
     * 生成query和countQuery，前提条件是先调用QueryRuleGroup.toSmartHqlQuery()再调用此方法。
     * 
     * @param entityClass
     * @param orderby
     * @param distinct
     */
    public void genetateQuery(Class<?> entityClass, Orderby orderby, boolean distinct) {
        genetatePageAggregateQuery(entityClass, orderby, distinct, null);
    }

    /**
     * 生成query,countQuery,summaryQuery，前提条件是先调用QueryRuleGroup.toSmartHqlQuery()再调用此方法。
     * 
     * @param entityClass
     * @param orderby
     * @param distinct
     * @param aggregateFields
     */
    public void genetatePageAggregateQuery(Class<?> entityClass, Orderby orderby, boolean distinct,
            List<FieldAggregateDescriptor> aggregateFields) {
        StringBuffer hql = new StringBuffer();
        // FROM
        hql.append("FROM " + entityClass.getName() + " o ");
        // JOIN
        if (this.hasJoins()) {
            for (String alias : this.getJoins().keySet()) {
                hql.append(this.getJoin(alias)).append(" ");
            }
        }
        // WHERE
        hql.append(getConditionString());

        this.countQuery = "SELECT COUNT(o.id) " + hql.toString();
        log.debug("********** Count query hql:" + this.countQuery);

        if (aggregateFields != null && !aggregateFields.isEmpty()) {
            this.aggregateQuery = genetateAggregate(aggregateFields) + hql.toString();
            log.debug("********** Aggregate query hql:" + this.aggregateQuery);
        }

        // ORDER BY
        orderby(orderby, hql);
        this.query = (distinct ? "SELECT distinct o " : "SELECT o ") + hql.toString();
        log.debug("********** Query hql:" + this.query);
        log.debug("********** Query Args:" + getArgsMessage(this.args));
    }

    private String genetateAggregate(List<FieldAggregateDescriptor> summaryFields) {
        StringBuffer buffer = new StringBuffer();
        if (summaryFields != null && !summaryFields.isEmpty()) {
            buffer.append("SELECT ");
            for (int i = 0; i < summaryFields.size(); i++) {
                FieldAggregateDescriptor fsd = summaryFields.get(i);
                buffer.append(fsd.getAggregate()).append("(o.").append(fsd.getFieldName()).append("),");
            }
            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private String getArgsMessage(List<Object> args) {
        StringBuffer sb = new StringBuffer();
        if (args != null && args.size() > 0) {
            for (Object arg : args) {
                sb.append(arg.toString()).append(";");
            }
        }
        return sb.toString();
    }

    /**
     * @param orderby
     * @param hql
     */
    public void orderby(Orderby orderby, StringBuffer hql) {
        if (orderby != null && orderby.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(orderby.getOrderby(), IConstants.COMMA);
            String[] orderArray = StringUtils.split(orderby.getOrder(), IConstants.COMMA);

            Assert.isTrue(orderByArray.length == orderArray.length || orderArray.length == 1,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
            boolean ordersame = orderArray.length == 1;

            hql.append(" ORDER BY ");
            for (int i = 0; i < orderByArray.length; i++) {
                if (i != 0) {
                    hql.append(",");
                }
                String orderBy = getOrderbyWithAlias(orderByArray[i]);
                hql.append(orderBy).append(" ").append(ordersame ? orderArray[0] : orderArray[i]);
            }
        }
    }

    /**
     * 给orderBy加上alias
     * 
     * @param orderBy
     * @return alias + orderBy
     */
    protected String getOrderbyWithAlias(String orderBy) {
        String result = orderBy;
        if (!orderBy.startsWith(QueryRule.DEFAULT_ALIAS_NAME)) {
            result = QueryRule.DEFAULT_ALIAS_NAME + "." + orderBy;
        }
        return result;
    }

    /**
     * 
     * @param orderby
     */
    public void orderby(Orderby orderby) {
        this.orderby(orderby, this.sql);
    }

    /**
     * 得到sql
     * 
     * @return sql
     */
    public String getSql() {
        return sql == null ? "" : sql.toString();
    }

    /**
     * 得到sql语句对应的参数数组
     * 
     * @return args
     */
    public Object[] getArgsArray() {
        if (args != null && args.size() > 0) {
            return args.toArray();
        } else {
            return null;
        }
    }

    /**
     * 得到不包含OrderBy部分的where语句。
     * 
     * @return sql
     */
    public String getNoOrderbyWhereSql() {
        String s = null;
        if (sql.length() > 0) {
            String whereCnd = sql.toString();
            String orderby = SqlUtil.fetchOrderby(whereCnd);
            if (!Strings.isBlank(orderby)) {
                s = StringUtils.substringBefore(whereCnd, orderby);
            }
        }
        return s;
    }

    public QueryHelper appendSql(Object s) {
        sql.append(s);
        return this;
    }

    public QueryHelper appendArg(Object arg) {
        args.add(arg);
        return this;
    }

    /**
     * @return the args
     */
    public List<Object> getArgs() {
        return args;
    }

    /**
     * @return the joins
     */
    public Map<String, String> getJoins() {
        return joins;
    }

    /**
     * 得到join语句
     * 
     * @param alias
     * @return join
     */
    public String getJoin(String alias) {
        return this.joins.get(alias);
    }

    /**
     * 加入join语句
     * 
     * @param alias
     * @param join
     */
    public void putJoin(String alias, String join) {
        this.joins.put(alias, join);
    }

    /**
     * 判断是否有join语句
     * 
     * @return true or false.
     */
    public boolean hasJoins() {
        return this.joins != null && this.joins.size() > 0;
    }

    /**
     * 检查alias是否存在，若不存在则添加join并返回自身，若存在则添加“_1”后缀继续递归判断。
     * 
     * @param alias
     * @param field
     * @param parentAlias
     * @return uniqueAlias
     */
    public String checkUniqueAlias(String alias, String field, String parentAlias) {
        String join = " join " + parentAlias + "." + field + " as " + alias;
        String oldJoin = this.getJoin(alias);
        if (oldJoin == null) {
            this.putJoin(alias, join);
            return alias;
        }

        if (!oldJoin.equals(join)) {
            String newAlias = alias + "_1";
            return checkUniqueAlias(newAlias, field, parentAlias);
        } else {
            return alias;
        }
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return the countQuery
     */
    public String getCountQuery() {
        return countQuery;
    }

    /**
     * @return the aggregateQuery
     */
    public String getAggregateQuery() {
        return aggregateQuery;
    }

}
