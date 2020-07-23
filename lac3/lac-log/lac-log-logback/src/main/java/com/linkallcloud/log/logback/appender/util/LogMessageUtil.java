package com.linkallcloud.log.logback.appender.util;

import org.slf4j.helpers.MessageFormatter;

import com.linkallcloud.core.busilog.util.LogExceptionStackTrace;
import com.linkallcloud.core.busilog.util.TraceLogMessageFactory;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.log.core.constant.LogMessageConstant;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;

public class LogMessageUtil {

    public static LacBusiLog getLogMessage(final String appName, String appType, final ILoggingEvent iLoggingEvent) {
        String formattedMessage = getMessage(iLoggingEvent);
        LacBusiLog logMessage =
                TraceLogMessageFactory.getLogMessage(appName, appType, formattedMessage, iLoggingEvent.getTimeStamp());
        logMessage.setClassName(iLoggingEvent.getLoggerName());
        StackTraceElement atackTraceElement = iLoggingEvent.getCallerData()[0];
        String method = atackTraceElement.getMethodName();
        String line = String.valueOf(atackTraceElement.getLineNumber());
        logMessage.setMethodName(method + "(" + atackTraceElement.getFileName() + ":" + line + ")");
        logMessage.setLogLevel(iLoggingEvent.getLevel().toString());
        return logMessage;
    }

    private static String getMessage(ILoggingEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[] {
                        LogExceptionStackTrace.erroStackTrace(throwableProxy.getThrowable()).toString() };
                return packageMessage(logEvent.getMessage(), args);
            } else {
                Object[] args = logEvent.getArgumentArray();
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof Throwable) {
                            args[i] = LogExceptionStackTrace.erroStackTrace(args[i]);
                        }
                    }
                    return packageMessage(logEvent.getMessage(), args);
                }
            }
        }
        return logEvent.getFormattedMessage();
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.indexOf(LogMessageConstant.DELIM_STR) > 0) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return TraceLogMessageFactory.packageMessage(message, args);
    }
}
