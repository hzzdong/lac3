package com.linkallcloud.web.interceptors;

import java.util.List;

public class WechatLoginInterceptor extends LoginInterceptor {

    public WechatLoginInterceptor() {
        super();
    }

    public WechatLoginInterceptor(List<String> ignoreRes, boolean override, String login, String noPermission) {
        super(ignoreRes, override, login, noPermission);
    }

    public WechatLoginInterceptor(List<String> ignoreRes, boolean override) {
        super(ignoreRes, override);
    }

    public WechatLoginInterceptor(String login, String noPermission) {
        super(login, noPermission);
    }

}
