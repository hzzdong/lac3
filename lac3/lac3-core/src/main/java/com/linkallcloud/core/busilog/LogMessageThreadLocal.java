package com.linkallcloud.core.busilog;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.linkallcloud.core.laclog.BusiLog;

public class LogMessageThreadLocal {
    public static TransmittableThreadLocal<BusiLog> logMessageThreadLocal = new TransmittableThreadLocal<>();
}
