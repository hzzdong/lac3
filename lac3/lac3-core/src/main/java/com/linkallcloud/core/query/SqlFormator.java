package com.linkallcloud.core.query;

import java.util.regex.Pattern;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;


/**
 * 提供了 Sql 相关的帮助函数
 * 
 */
public class SqlFormator {

    private static final Pattern CND = Pattern.compile("^([ \t]*)(WHERE|ORDER BY)(.+)$", Pattern.CASE_INSENSITIVE);

    /**
     * @param en
     *            实体
     * @return WHERE 子句
     */
    public static String getConditionString(String cnd) {
        if (!Strings.isBlank(cnd)) {
            if (!CND.matcher(cnd).find()) {
                return " WHERE " + cnd;
            }
            return cnd;
        }
        return "";
    }

    /**
     * @param v
     *            字段值
     * @return 格式化后的 Sql 字段值，可以直接拼装在 SQL 里面
     */
    public static CharSequence formatFieldValue(Object v) {
        if (null == v) {
            return "NULL";
        } else if (SqlFormator.isNotNeedQuote(v.getClass())) {
            return SqlFormator.escapeFieldValue(v.toString());
        } else {
            return new StringBuilder("'").append(v.toString()).append('\'');
        }
        // return new StringBuilder("'").append(
        // SqlFormator.escapeFieldValue(v.toString())).append('\'');
    }

    /**
     * 
     * @param value
     *            字段值
     * @param notNeedQuote
     * @return 格式化后的 Sql 字段值，可以直接拼装在 SQL 里面
     */
    public static CharSequence formatFieldValue(Object value, boolean notNeedQuote) {
        if (notNeedQuote) {
            return value.toString();
        } else {
            // return String.format("'%s'", SqlFormator
            // .escapeFieldValue(Castors.me().castToString(value)));
            return String.format("'%s'", Castors.me().castToString(value));
        }
    }

    /**
     * 将 SQL 的字段值进行转意，可以用来防止 SQL 注入攻击
     * 
     * @param s
     *            字段值
     * @return 格式化后的 Sql 字段值，可以直接拼装在 SQL 里面
     */
    public static CharSequence escapeFieldValue(CharSequence s) {
        StringBuilder sb = null;
        if (null != s) {
            sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\'') {
                    sb.append('\'').append('\'');
                } else if (c == '\\') {
                    sb.append('\\').append('\\');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb;
    }

    /**
     * 将 SQL 的 WHERE 条件值进行转意，可以用来防止 SQL 注入攻击
     * 
     * @param s
     *            字段值
     * @return 格式化后的 Sql 字段值，可以直接拼装在 SQL 里面
     */
    public static CharSequence escapteConditionValue(CharSequence s) {
        if (null == s) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\'') {
                sb.append('\'').append('\'');
            } else if (c == '\\') {
                sb.append('\\').append('\\');
            } else if (c == '_') {
                sb.append('\\').append(c);
            } else if (c == '%') {
                sb.append('\\').append(c);
            } else {
                sb.append(c);
            }
        }
        return sb;
    }

    /**
     * 判断一个值，在 SQL 中是否需要单引号
     * 
     * @param type
     *            类型
     * @return 是否需要加上单引号
     */
    public static boolean isNotNeedQuote(Class<?> type) {
        Mirror<?> me = Mirror.me(type);
        return me.isBoolean() || me.isPrimitiveNumber();
    }

    /**
     * 将一个数组转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     * 
     * @param c
     *            分隔符
     * @param objs
     *            数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concatINSql(Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || 0 == objs.length) {
            return sb;
        }

        boolean notNeedQuote = SqlFormator.isNotNeedQuote(objs[0].getClass());
        sb.append("(").append(SqlFormator.formatFieldValue(objs[0], notNeedQuote));
        for (int i = 1; i < objs.length; i++) {
            sb.append(c).append(SqlFormator.formatFieldValue(objs[i], notNeedQuote));
        }
        sb.append(")");
        return sb;
    }

    /**
     * 将一个数组转换成 in+字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     * 
     * @param c
     *            分隔符
     * @param objs
     *            数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder inSql(Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || 0 == objs.length) {
            return sb;
        }

        boolean notNeedQuote = SqlFormator.isNotNeedQuote(objs[0].getClass());
        sb.append(" IN (").append(SqlFormator.formatFieldValue(objs[0], notNeedQuote));
        for (int i = 1; i < objs.length; i++) {
            sb.append(c).append(SqlFormator.formatFieldValue(objs[i], notNeedQuote));
        }
        sb.append(")");
        return sb;
    }    
    
    // public static Object escapteNullValue(Class<?> type, Object value) {
    // if (null == value) {
    // Mirror<?> me = Mirror.me(type);
    // if (me.isString() || me.isStringLike() || me.isChar()) {
    // return "";
    // }else{
    // return null;
    // }
    // } else {
    // return value;
    // }
    // }
}
