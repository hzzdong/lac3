package com.linkallcloud.core.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.rule.*;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;
import com.linkallcloud.core.util.IConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query implements Expression {
    private static final long serialVersionUID = 4771222508112466306L;

    public enum GroupOperator {
        AND, OR
    }

    private List<QueryRule> rules;
    private List<Query> groups;
    private GroupOperator groupOp;

    private boolean distinct;
    private boolean mapUnderscoreToCamelCase;

    // 排序条件（非datatables）
    private Orderby orderby;

    // cache field
    private Map<String, Object> cachedParams = null;
    private String[] cachedOrders = null;

    public Query() {
        this.groupOp = GroupOperator.AND;
    }

    public Query(GroupOperator groupOp) {
        this.groupOp = groupOp;
    }

    public Query(String groupOp) {
        setGroupOp(groupOp);
    }

    public GroupOperator getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        try {
            this.groupOp = Enum.valueOf(GroupOperator.class, groupOp);
        } catch (Throwable e) {
            throw new IllegalArgumentException("groupOp(" + groupOp + ") not support.", e);
        }
    }

    public void setGroupOp(GroupOperator groupOp) {
        this.groupOp = groupOp;
    }

    public List<Query> getGroups() {
        return this.groups;
    }

    public void addGroup(Query group) {
        if (this.groups == null) {
            this.groups = new ArrayList<Query>();
        }
        this.groups.add(group);
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public List<QueryRule> getRules() {
        return this.rules;
    }

    public void addRule(QueryRule rule) {
        if (this.rules == null) {
            this.rules = new ArrayList<QueryRule>();
        }
        this.rules.add(rule);
    }

    public void addRule(IRuleDescriptor ruleDesc) {
        if (ruleDesc != null) {
            QueryRule[] qrs = QueryRuleFactory.create(ruleDesc);
            if (qrs != null && qrs.length > 0) {
                for (QueryRule qr : qrs) {
                    addRule(qr);
                }
            }
        }
    }

    public Orderby getOrderby() {
        return orderby;
    }

    public void setOrderby(Orderby orderby) {
        this.orderby = orderby;
    }

    public void setRules(List<QueryRule> rules) {
        this.rules = rules;
    }

    public void setGroups(List<Query> groups) {
        this.groups = groups;
    }

    /**
     * 得到所有的查询条件
     *
     * @return
     */
    @JSONField(serialize = false)
    public Map<String, Object> getCnds() {
        if (this.cachedParams != null) {
            return this.cachedParams;
        } else {
            Map<String, Object> temp = new HashMap<String, Object>();
            if (this.hasQueryConditons()) {
                this.outputQueryParameters(temp);
            }
            this.cachedParams = temp;
            return this.cachedParams;
        }
    }

    /**
     * 得到排序sql
     *
     * @return
     */
    @JSONField(serialize = false)
    public String[] getOrders() {
        if (this.cachedOrders != null) {
            return this.cachedOrders;
        } else {
            String[] temp = new String[0];
            if (orderby != null && orderby.isOrderBySetted()) {
                temp = orderby.getOrderSqls(isMapUnderscoreToCamelCase());
            }
            this.cachedOrders = temp;
            return this.cachedOrders;
        }
    }

    /**
     * order sql, 比如：id desc,name asc
     *
     * @return
     */
    @JSONField(serialize = false)
    public String getOrdersSql() {
        String[] orderbys = getOrders();
        if (orderbys != null && orderbys.length > 0) {
            return Strings.join2(",", orderbys);
        }
        return null;
    }

    /**
     * 是否存在名称是fieldName的rule
     *
     * @param fieldName
     * @return boolean
     */
    public boolean hasRule4Field(String fieldName) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (QueryRule rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                    return true;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (Query group : getGroups()) {
                if (group.hasRule4Field(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 得到名称是fieldName的rule
     *
     * @param fieldName
     * @return boolean
     */
    public QueryRule getRule4Field(String fieldName) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (QueryRule rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                    return rd;
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (Query group : getGroups()) {
                QueryRule r = group.getRule4Field(fieldName);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * 重新设置名称是fieldName的rule的值
     *
     * @param fieldName
     * @param value
     */
    public void resetRule4Field(String fieldName, Object value) {
        if (getRules() != null && !getRules().isEmpty()) {
            for (QueryRule rd : getRules()) {
                if (rd.getField().equals(fieldName)) {
                    if (rd instanceof CompareRule) {
                        CompareRule cr = (CompareRule) rd;
                        cr.setValue(value);
                        return;
                    }
                }
            }
        }
        if (getGroups() != null && !getGroups().isEmpty()) {
            for (Query group : getGroups()) {
                group.resetRule4Field(fieldName, value);
            }
        }
    }

    /*-
     *  示例，若界面输入条件如下：
     * "where":
     *   {
     *       "groupOp":"AND",
     *       "rules":[
     *           {"field":"role.code","op":"eq","data":"r_admin"},//也可以{"field":"role#code","op":"eq","data":"r_admin"},
     *           {"field":"RoleCompany.tel","op":"eq","data":"87396727"}//也可以{"field":"role_company#tel","op":"eq","data":"87396727"}
     *       ],
     *       "groups":[
     *           {
     *               "groupOp":"OR",
     *               "rules":[
     *                   {"field":"o.name","op":"cn","data":"同学"},
     *                   {"field":"o.age","op":"ge","data":"15"}
     *               ],
     *               "groups":[]
     *           }
     *       ]
     *   }
     * 输出Where sql条件部分：
     * ( role.code=? AND role_company.tel=? AND (o.name Like ? OR o.age>=?) )
     *
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryExpression#toWhere(com.linkallcloud.dao.query.Where, boolean)
     */
    @Override
    public void toWhere(QueryHelper where, boolean jdbcMappingField) {
        if (hasQueryConditons()) {
            where.appendSql(IConstants.LEFT_PARENTHESIS);// (
            boolean firstChild = true;
            // rules
            if (this.getRules() != null && this.getRules().size() > 0) {
                for (QueryRule qr : this.getRules()) {
                    firstChild = andOr(where, firstChild);
                    qr.toWhere(where, jdbcMappingField);
                }
            }
            // groups
            if (this.getGroups() != null && this.getGroups().size() > 0) {
                for (Query group : this.getGroups()) {
                    firstChild = andOr(where, firstChild);
                    group.toWhere(where, jdbcMappingField);
                }
            }
            where.appendSql(IConstants.RIGHT_PARENTHESIS);// )
        }
    }

    private boolean andOr(QueryHelper where, boolean b) {
        if (!b) {
            where.appendSql(GroupOperator.AND.equals(this.groupOp) ? " AND " : " OR ");// AND
            // -
            // OR
        }
        return false;
    }

    /*-
     *   示例，若界面输入条件如下：
     * "where":
     *   {
     *       "groupOp":"AND",
     *       "rules":[
     *           {"field":"role.code","op":"eq","data":"r_admin"},//也可以{"field":"role#code","op":"eq","data":"r_admin"},
     *           {"field":"role.company.tel","op":"eq","data":"87396727"}//也可以{"field":"role#company#tel","op":"eq","data":"87396727"}
     *       ],
     *       "groups":[
     *           {
     *               "groupOp":"OR",
     *               "rules":[
     *                   {"field":"name","op":"cn","data":"同学"},
     *                   {"field":"age","op":"ge","data":"15"}
     *               ],
     *               "groups":[]
     *           }
     *       ]
     *   }
     * 输出Where sql条件部分：
     * ( role.code=? AND role.company.tel=? AND (o.name Like ? OR o.age>=?) )
     *
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryExpression#toWhere4Hql(com.linkallcloud.dao.query.Where)
     */
    @Override
    public void toWhere4Hql(QueryHelper where) {
        if (hasQueryConditons()) {
            where.appendSql(IConstants.LEFT_PARENTHESIS);// (
            boolean firstChild = true;
            // rules
            if (this.getRules() != null && this.getRules().size() > 0) {
                for (QueryRule qr : this.getRules()) {
                    firstChild = andOr(where, firstChild);
                    qr.toWhere4Hql(where);
                }
            }
            // groups
            if (this.getGroups() != null && this.getGroups().size() > 0) {
                for (Query group : this.getGroups()) {
                    firstChild = andOr(where, firstChild);
                    group.toWhere4Hql(where);
                }
            }
            where.appendSql(IConstants.RIGHT_PARENTHESIS);// )
        }
    }

    /**
     * 是否有查询条件
     *
     * @return boolean
     */
    public boolean hasQueryConditons() {
        if (this.getRules() != null && this.getRules().size() > 0) {
            return true;
        } else {
            if (this.getGroups() != null && this.getGroups().size() > 0) {
                for (Query g : this.getGroups()) {
                    if (g.hasQueryConditons()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.linkallcloud.dao.query.QueryExpression#toSmartHqlQuery(com.
     * linkallcloud.dao .query.Where)
     */
    public void toSmartHqlQuery(QueryHelper where) {
        if (hasQueryConditons()) {
            where.appendSql(IConstants.LEFT_PARENTHESIS);// (
            boolean firstChild = true;
            // rules
            if (this.getRules() != null && this.getRules().size() > 0) {
                for (QueryRule qr : this.getRules()) {
                    firstChild = andOr(where, firstChild);
                    qr.toSmartHqlQuery(where);
                }
            }
            // groups
            if (this.getGroups() != null && this.getGroups().size() > 0) {
                for (Query group : this.getGroups()) {
                    firstChild = andOr(where, firstChild);
                    group.toSmartHqlQuery(where);
                }
            }
            where.appendSql(IConstants.RIGHT_PARENTHESIS);// )
        }
    }

    /**
     * 输出查询条件到cndsMap
     *
     * @param cndsMap
     * @param mapUnderscoreToCamelCase
     */
    public void outputQueryParameters(Map<String, Object> cndsMap) {
        if (this.hasQueryConditons()) {
            if ((this.getRules() != null) && (this.getRules().size() > 0)) {
                for (QueryRule qr : this.getRules()) {
                    if (qr instanceof CompareRule) {
                        CompareRule cr = (CompareRule) qr;
                        String dbFieldName = cr.getField();
                        if (this.mapUnderscoreToCamelCase) {
                            dbFieldName = Strings.lowerWord(dbFieldName, '_');
                        }
                        if (qr instanceof In && cr.getValue() != null) {
                            cndsMap.put(dbFieldName, Castors.me().castTo(cr.getValue(), ArrayList.class));
                        } else {
                            cndsMap.put(dbFieldName, cr.getCompareValue());
                        }

                    } else if (qr instanceof ExpRule) {
                        ExpRule cr = (ExpRule) qr;
                        String dbFieldName = cr.getField();
                        if (this.mapUnderscoreToCamelCase) {
                            dbFieldName = Strings.lowerWord(dbFieldName, '_');
                        }
                        if (qr instanceof IsNull) {
                            cndsMap.put(dbFieldName + "IsN", true);
                        } else if (qr instanceof IsNotNull) {
                            cndsMap.put(dbFieldName + "IsNN", true);
                        }
                    }
                }
            }

            if ((this.getGroups() != null) && (this.getGroups().size() > 0)) {
                for (Query group : this.getGroups()) {
                    group.outputQueryParameters(cndsMap);
                }
            }
        }
    }

}
