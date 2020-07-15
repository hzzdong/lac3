package com.linkallcloud.core.query;

public enum Operator {
    eq /* = */, ne /* <> */, lt /* < */, le /* <= */, gt /* > */, ge/* >= */,

    in /* IN */, ni/* NOT IN */,

    nu/* IS NULL */, nn/* IS NOT NULL */,

    bw /* LIKE(begins with) */, bn /* NOT LIKE(does not begin with) */,

    ew/* LIKE(ends with) */, en/* NOT LIKE(does not end with) */,

    cn/* LIKE(contains) */, nc/* NOT LIKE(does not contain) */;
}
