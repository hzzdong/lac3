package com.linkallcloud.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.linkallcloud.core.lang.Strings;
import org.apache.commons.lang3.StringUtils;

public class SqlUtil {

    private static final String FROM_REG_EX = "from ";

    /**
     * 本函数只能自动处理简单的sql语句,复杂的sql查询请另行编写count语句查询。 select子句与order by子句会影响count查询,进行简单的排除.
     * 
     * @param sql
     * @return count sql
     */
    public static String parseCountSqlFromSourceSql(final String sql) {
        String fromSql = sql;
        String from = SqlUtil.fetchFrom(sql);
        if (!Strings.isBlank(from)) {
            fromSql = FROM_REG_EX + StringUtils.substringAfter(fromSql, from);
        }
        String orderby = SqlUtil.fetchOrderby(fromSql);
        if (!Strings.isBlank(orderby)) {
            fromSql = StringUtils.substringBefore(fromSql, orderby);
        }
        return "select count(0) " + fromSql;
    }

    /**
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询. select子句与order by子句会影响count查询,进行简单的排除.
     * 
     * @param hsql
     * @return count sql
     */
    public static String parseCountHsqlFromSourceHsql(final String hsql) {
        String fromHql = hsql;
        String from = SqlUtil.fetchFrom(hsql);
        if (!Strings.isBlank(from)) {
            fromHql = FROM_REG_EX + StringUtils.substringAfter(fromHql, from);
        }
        String orderby = SqlUtil.fetchOrderby(fromHql);
        if (!Strings.isBlank(orderby)) {
            fromHql = StringUtils.substringBefore(fromHql, orderby);
        }

        // fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        // fromHql = StringUtils.substringBefore(fromHql, "order by");

        return "select count(*) " + fromHql;
    }

    /**
     * 从sql中匹配“from”，并返回“from”
     * 
     * @param sql
     * @return from str
     */
    public static String fetchFrom(String sql) {
        Pattern p = Pattern.compile(FROM_REG_EX, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    /**
     * 从sql中匹配“order by”，并返回““order by””
     * 
     * @param sql
     * @return “order by”
     */
    public static String fetchOrderby(String sql) {
        String regEx = " order( +)by ";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

}
