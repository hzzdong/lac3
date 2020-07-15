package com.linkallcloud.core.query;

import java.io.Serializable;

import com.linkallcloud.core.lang.Strings;
import org.apache.commons.lang3.StringUtils;

public class Orderby implements Serializable {
    private static final long serialVersionUID = 860574054021907877L;

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private String orderby = "";// 排序字段,无默认值.多个排序字段时用','分隔.
    private String order = "";// asc or desc ，多個之間用","分隔

    public Orderby() {
        super();
    }

    /**
     * 
     * @param orderBy
     * @param order
     */
    public Orderby(String orderBy, String order) {
        super();
        setOrderby(orderBy);
        setOrder(order);
    }

    public Orderby orderby(final String theOrderBy) {
        setOrderby(theOrderBy);
        return this;
    }

    public Orderby order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     * 是否已设置排序字段,无默认值.
     * 
     * @return boolean
     */
    public boolean isOrderBySetted() {
        return !Strings.isBlank(orderby) && !Strings.isBlank(order);
    }

    /**
     * @return the orderBy
     */
    public String getOrderby() {
        return orderby;
    }

    public String getOrderField() {
        return Strings.splitWordsWithUnderline(orderby);
    }

    /**
     * @param orderby
     *            the orderby to set
     */
    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    public String getOrderDir() {
        return order;
    }

    /**
     * 设置排序方式向.
     * 
     * @param order
     *            可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        // 检查order字符串的合法值
        String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.order = StringUtils.lowerCase(order);
    }

    /**
     * 
     * @param mapUnderscoreToCamelCase
     * @return
     */
    public String[] getOrderSqls(boolean mapUnderscoreToCamelCase) {
        // String[] orders = new
        if (!Strings.isBlank(this.orderby)) {
            String[] orderSqls = StringUtils.split(this.orderby, ',');
            if (orderSqls != null && orderSqls.length > 0) {
                if (mapUnderscoreToCamelCase) {
                    for (int i = 0; i < orderSqls.length; i++) {
                        orderSqls[i] = Strings.lowerWord(orderSqls[i], '_');
                    }
                }

                if (orderSqls.length == 1) {
                    if (!Strings.isBlank(this.order)) {
                        orderSqls[0] = orderSqls[0] + " " + this.order;
                    }
                } else if (orderSqls.length == 2) {
                    if (!Strings.isBlank(this.order)) {
                        String[] orders = StringUtils.split(this.order, ',');
                        if (orders != null) {
                            if (orders.length == 1) {
                                orderSqls[0] = orderSqls[0] + " " + this.order;
                                orderSqls[1] = orderSqls[1] + " " + this.order;
                            } else if (orders.length == 2) {
                                orderSqls[0] = orderSqls[0] + " " + orders[0];
                                orderSqls[1] = orderSqls[1] + " " + orders[1];
                            }
                        }
                    }
                }
                return orderSqls;
            }
        }
        return null;
    }

}
