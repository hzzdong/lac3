package com.linkallcloud.core.query.rule.desc;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Operator;

/**
 * 查询规则描述：支持多字段，操作和值相同的规则，比如:{"field":"name,addr","op":"cn","data":"hangzhou", type:"S,S"}
 * 
 */
public class StringRuleDescriptor extends RuleDescriptor {

    private String op;// 操作
    // private String data;// 字段值：界面上传进来的字符串
    private Object data;// 字段值：界面上传进来的字符串

    public StringRuleDescriptor() {
        super();
    }

    /**
     * 
     * @param field
     * @param op
     * @param data
     * @param fieldType
     */
    public StringRuleDescriptor(String field, String op, String data, String fieldType) {
        super(field, fieldType);
        this.op = op;
        this.data = data;
    }

    /**
     * 
     * @param field
     * @param op
     * @param data
     * @param fieldType
     */
    public StringRuleDescriptor(String field, String op, Object data, String fieldType) {
        super(field, fieldType);
        this.op = op;
        this.data = data;
        // if (data != null) {
        // if (op == Operator.in.name() || op != Operator.ni.name()) {
        // if (data instanceof Collection) {
        // this.data = JSON.toJSONString(((Collection) data).toArray());
        // } else if (data.getClass().isArray()) {
        // this.data = JSON.toJSONString(data);
        // } else {
        // throw new IllegalParameterRuntimeException(
        // IllegalParameterRuntimeException.PARAMETER_RUNTIME_EXCEPTION, "数据只能为集合类型或数组");
        // }
        // } else {
        // this.data = String.valueOf(data);
        // }
        //
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getOperator()
     */
    @Override
    public Operator getOperator() {
    	if(Strings.isBlank(this.op)) {
    		return Operator.eq;
    	}
    	
        Operator result = null;
        try {
            result = Enum.valueOf(Operator.class, this.op);
        } catch (Throwable e) {
            throw new IllegalArgumentException("op(" + op + ") not support.", e);
        }
        return result;
    }

    /**
     * @return the op
     */
    public String getOp() {
        return op;
    }

    /**
     * @param op
     *            the op to set
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getValue()
     */
    @Override
    public Object getValue() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        if (value != null) {
            this.data = value.toString();
        }
    }

}
