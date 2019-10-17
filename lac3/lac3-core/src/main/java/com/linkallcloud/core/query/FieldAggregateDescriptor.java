package com.linkallcloud.core.query;

public class FieldAggregateDescriptor {

    private String aggregate;
    private String fieldName;
    private Class<?> fieldClass;

    private String columnName;
    private IFieldProcessor fieldProcessor;

    public FieldAggregateDescriptor(String aggregate, String fieldName, Class<?> fieldClass, String columnName) {
        super();
        this.aggregate = aggregate;
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.columnName = columnName;
    }

    public FieldAggregateDescriptor(String aggregate, String fieldName, Class<?> fieldClass) {
        this(aggregate, fieldName, fieldClass, fieldName);
    }

    public FieldAggregateDescriptor(String aggregate, String fieldName, Class<?> fieldClass, String columnName,
            IFieldProcessor fieldProcessor) {
        this(aggregate, fieldName, fieldClass, columnName);
        this.fieldProcessor = fieldProcessor;
    }

    public FieldAggregateDescriptor(String aggregate, String fieldName, Class<?> fieldClass,
            IFieldProcessor fieldProcessor) {
        this(aggregate, fieldName, fieldClass, fieldName, fieldProcessor);
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldClass
     */
    public Class<?> getFieldClass() {
        return fieldClass;
    }

    /**
     * @param fieldClass
     *            the fieldClass to set
     */
    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName
     *            the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the fieldProcessor
     */
    public IFieldProcessor getFieldProcessor() {
        return fieldProcessor;
    }

    /**
     * @param fieldProcessor
     *            the fieldProcessor to set
     */
    public void setFieldProcessor(IFieldProcessor fieldProcessor) {
        this.fieldProcessor = fieldProcessor;
    }

    /**
     * @return the aggregate
     */
    public String getAggregate() {
        return aggregate;
    }

    /**
     * @param aggregate
     *            the aggregate to set
     */
    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

}
