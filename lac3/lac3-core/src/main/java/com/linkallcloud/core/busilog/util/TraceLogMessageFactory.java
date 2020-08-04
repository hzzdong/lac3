package com.linkallcloud.core.busilog.util;

import java.util.Date;

import com.linkallcloud.core.laclog.BusiLog;

public class TraceLogMessageFactory<T> {

    public static BusiLog getLogMessage(String appName, String appType, String message, long time) {
    	BusiLog logMessage = new BusiLog();
        logMessage.setAppName(appName);
        logMessage.setAppType(appType);
        logMessage.setErrorMessage(message);
        logMessage.setOperateTime(new Date(time));
        logMessage.setDtTime(time);
        return logMessage;
    }

    public static String packageMessage(String message, Object[] args) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(message);
        for (int i = 0; i < args.length; i++) {
            builder.append("\n").append(args[i]);
        }
        return builder.toString();
    }

}
