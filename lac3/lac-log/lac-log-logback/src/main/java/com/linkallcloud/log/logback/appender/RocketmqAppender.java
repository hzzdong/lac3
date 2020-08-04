package com.linkallcloud.log.logback.appender;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.aop.LacAspect;
import com.linkallcloud.core.busilog.enums.LogMode;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.log.core.constant.LogMessageConstant;
import com.linkallcloud.log.core.rocketmq.RocketmqProducerClient;
import com.linkallcloud.log.logback.appender.util.LogMessageUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;

public class RocketmqAppender extends AppenderBase<ILoggingEvent> {
	private String appName;
	private String appType;
	private String mqGroupName;
	private String namesrvAddr;
	private String accessKey;
	private String secretKey;

	private static DefaultMQProducer producer = null;

	@Override
	protected void append(ILoggingEvent event) {
		try {
			if (event.getLevel().equals(Level.ERROR)) {
				ThrowableProxy throwableProxy = (ThrowableProxy) event.getThrowableProxy();
				BusiLog log = LacAspect.logMessageThreadLocal.get();
				if (log != null && throwableProxy != null && throwableProxy.getThrowable().equals(log.getError())) {
					return;
				}
				BusiLog logMessage = LogMessageUtil.getLogMessage(appName, appType, event);
				logMessage.setAppName(appName);
				logMessage.setAppType(appType);
				logMessage.setLogMode(LogMode.FILE.getCode());
				RocketmqProducerClient.getInstance().sendMsg(JSON.toJSONString(logMessage));
			}

		} catch (Exception e) {
			// FIXME 本地日志等后续处理
			addError("RocketmqAppender Error:", e);
		}
	}

	@Override
	public void start() {
		super.start();
		if (appName.indexOf(LogMessageConstant.KEY_SEPARATOR) > -1) {
			RuntimeException e = new RuntimeException("log appName 中不能包含:" + LogMessageConstant.KEY_SEPARATOR);
			addError("Log MQ 启动失败:", e);
			throw e;
		}
		if (appType.indexOf(LogMessageConstant.KEY_SEPARATOR) > -1) {
			RuntimeException e = new RuntimeException("log appType 中不能包含:" + LogMessageConstant.KEY_SEPARATOR);
			addError("Log MQ 启动失败:", e);
			throw e;
		}
		producer = new DefaultMQProducer(mqGroupName + "_producer",
				new AclClientRPCHook(new SessionCredentials(accessKey, secretKey)));
		producer.setNamesrvAddr(namesrvAddr);
		try {
			producer.start();
			RocketmqProducerClient.init(producer);
		} catch (Exception e) {
			addError("Log MQ 启动失败:", e);
			// RuntimeException ee = new RuntimeException("Log MQ 启动失败");
			// throw ee;
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (producer != null) {
			producer.shutdown();
		}
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setMqGroupName(String mqGroupName) {
		this.mqGroupName = mqGroupName;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public static DefaultMQProducer getProducer() {
		return producer;
	}
}
