package com.linkallcloud.core.lang;

@SuppressWarnings("serial")
public class FailToGetValueException extends RuntimeException {

    public FailToGetValueException(String message, Throwable e) {
        super(message, e);
    }

}
