package com.linkallcloud.core.query.rule.desc;

import com.linkallcloud.core.query.Operator;

public interface IRuleDescriptor {

    /**
     * 获取字段
     * 
     * @return fields
     */
    String getField();

    /**
     * 获取字段类型
     * 
     * @return types
     */
    String getType();

    /**
     * 获取操作
     * 
     * @return op
     */
    Operator getOperator();

    /**
     * 获取字段对应的值
     * 
     * @return Object
     */
    Object getValue();
    
    /**
     * 设置字段对应的值
     * 
     * @param value
     */
    void setValue(Object value);

    /**
     * 获取字段数组
     * 
     * @return fields
     */
    String[] getFields();
    
    /**
     * 获取类型数组
     * 
     * @return types
     */
    String[] getTypes();
    
    /**
     * 此rule是否包含fieldName的Field
     * 
     * @param fieldName
     * @return boolean
     */
    boolean isRule4Field(String fieldName);
}
