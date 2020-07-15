package com.linkallcloud.core.query.rule;

import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.query.Operator;
import com.linkallcloud.core.query.PropertyType;
import com.linkallcloud.core.query.rule.desc.IRuleDescriptor;
import com.linkallcloud.core.query.rule.desc.ObjectRuleDescriptor;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;

public class QueryRuleFactory {

    private static Map<String, Mirror<? extends QueryRule>> mirrors =
            new HashMap<String, Mirror<? extends QueryRule>>();

    private static Mirror<? extends QueryRule> getMirror(Class<? extends QueryRule> ruleClass) {
        String ruleClassName = ruleClass.getName();
        Mirror<? extends QueryRule> mirror = mirrors.get(ruleClassName);
        if (mirror == null) {
            synchronized (mirrors) {
                mirror = mirrors.get(ruleClassName);
                if (mirror == null) {
                    mirror = Mirror.me(ruleClass);
                    mirrors.put(ruleClassName, mirror);
                }
            }
        }
        return mirror;
    }

    /**
     * 根据Rule的描述对象,创建QueryRule对象
     * 
     * @param rd
     * @return QueryRule
     */
    public static QueryRule[] create(IRuleDescriptor rd) {
        if (rd != null) {
            if (Operator.eq.equals(rd.getOperator())) {// =
                return createRule(rd, Equal.class);
            } else if (Operator.ne.equals(rd.getOperator())) {// <>
                return createRule(rd, NotEqual.class);
            } else if (Operator.lt.equals(rd.getOperator())) {// <
                return createRule(rd, Less.class);
            } else if (Operator.gt.equals(rd.getOperator())) {// >
                return createRule(rd, Greater.class);
            } else if (Operator.le.equals(rd.getOperator())) {// <=
                return createRule(rd, LessEqual.class);
            } else if (Operator.ge.equals(rd.getOperator())) {// >=
                return createRule(rd, GreaterEqual.class);
            } else if (Operator.in.equals(rd.getOperator())) {// in
                return createRule(rd, In.class);
            } else if (Operator.ni.equals(rd.getOperator())) {// not in
                return createRule(rd, NotIn.class);
            } else if (Operator.nu.equals(rd.getOperator())) {// is null
                return createRule(rd, IsNull.class);
            } else if (Operator.nn.equals(rd.getOperator())) {// is not null
                return createRule(rd, IsNotNull.class);
            } else if (Operator.bw.equals(rd.getOperator())) {/* LIKE(begins with) */
                return createRule(rd, BeginsWith.class);
            } else if (Operator.bn.equals(rd.getOperator())) {/* NOT LIKE(does not begin with) */
                return createRule(rd, NotBeginsWith.class);
            } else if (Operator.ew.equals(rd.getOperator())) {/* LIKE(ends with) */
                return createRule(rd, EndsWith.class);
            } else if (Operator.en.equals(rd.getOperator())) {/* NOT LIKE(does not end with) */
                return createRule(rd, NotEndsWith.class);
            } else if (Operator.cn.equals(rd.getOperator())) {/* LIKE(contains) */
                return createRule(rd, Contains.class);
            } else if (Operator.nc.equals(rd.getOperator())) {/* NOT LIKE(does not contain) */
                return createRule(rd, NotContains.class);
            }
        }
        return null;
    }

    /**
     * 
     * @param rd
     * @param ruleClass
     * @return QueryRule[]
     */
    private static QueryRule[] createRule(IRuleDescriptor rd, Class<? extends QueryRule> ruleClass) {
        String[] fields = rd.getFields();
        if (fields == null || fields.length <= 0) {// || rd.getValue() == null
            return null;
        }

        QueryRule[] result = null;
        Mirror<? extends QueryRule> mirror = getMirror(ruleClass);
        if (fields.length == 1) {
            result = new QueryRule[1];
            result[0] = mirror.born(rd);
        } else {
            result = new QueryRule[fields.length];

            String[] types = parseTypes(rd);
            // 若length==1或者和字段长度不一致，则以数组的第一个元素作为一致的类型
            boolean sameType = (types.length == 1 || types.length <= fields.length);

            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];
                if (rd instanceof ObjectRuleDescriptor) {
                    result[i] = mirror.born(field, rd.getOperator(), rd.getValue());
                } else if (rd instanceof StringRuleDescriptor) {
                    String typeName = sameType ? types[0] : types[i];
                    result[i] =
                            mirror.born(field, rd.getOperator(), ((StringRuleDescriptor) rd).getData(),
                                    convert(typeName));
                }
            }
        }
        return result;
    }

    /**
     * 若rd.getTypes()为空则设置默认为String类型，否则直接返回rd.getTypes()
     * 
     * @param rd
     * @return String[]
     */
    private static String[] parseTypes(IRuleDescriptor rd) {
        String[] types = rd.getTypes();
        if (types == null || types.length <= 0) {
            types = new String[1];
            types[0] = "S";
        }
        return types;
    }

    /**
     * 把字段类型字符串转换成Class<?>
     * 
     * @param typeName
     * @return Class
     */
    private static Class<?> convert(String typeName) {
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
        return typeClass;
    }

}
