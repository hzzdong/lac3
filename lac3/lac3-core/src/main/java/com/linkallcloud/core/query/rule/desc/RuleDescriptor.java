package com.linkallcloud.core.query.rule.desc;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.IConstants;

public abstract class RuleDescriptor implements IRuleDescriptor {

    private String field;// 字段
    private String type;// 字段类型

    public RuleDescriptor() {
    }

    public RuleDescriptor(String field, String fieldType) {
        this.setField(field);
        this.setType(fieldType);
    }

    /**
     * 得到字段数组，支持“,”分割方式：name,age
     * 
     * @return fields
     */
    public String[] getFields() {
        return Strings.isBlank(this.field) ? null : Strings.trimAll(this.field).split(String.valueOf(IConstants.COMMA));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getTypes()
     */
    @Override
    public String[] getTypes() {
        return Strings.isBlank(this.type) ? null : Strings.trimAll(this.type).split(String.valueOf(IConstants.COMMA));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#isRule4Field(java.lang.String)
     */
    @Override
    public boolean isRule4Field(String fieldName) {
        String[] fields = getFields();
        if (fields != null && fields.length >= 1) {
            for (String field : fields) {
                if (fieldName.equals(field)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getField()
     */
    @Override
    public String getField() {
        return this.field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkallcloud.dao.query.rule.desc.IRuleDescriptor#getType()
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * @param field
     *            the field to set
     */
    public void setField(String field) {
        this.field = field.trim();
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

}
