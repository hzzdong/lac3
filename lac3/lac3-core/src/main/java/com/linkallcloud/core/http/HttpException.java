package com.linkallcloud.core.http;

@SuppressWarnings("serial")
public class HttpException extends RuntimeException {

    public HttpException(String url, Throwable cause) {
        super("url="+url, cause);
    }

}
