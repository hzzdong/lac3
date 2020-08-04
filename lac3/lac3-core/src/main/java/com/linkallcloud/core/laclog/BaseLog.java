package com.linkallcloud.core.laclog;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;

public abstract class BaseLog extends Domain {
	private static final long serialVersionUID = -6428103666648336600L;

	/**
	 * 是否已经被存储（可能会有多个拦截器拦截到该记录，如果已经被记录后续就不在重复记录）
	 */
	@JSONField(serialize = false, deserialize = false)
	private boolean alreadyStored;
	@JSONField(serialize = false, deserialize = false)
	private Throwable error;// 方法自行失败的错误
	/**
	 * 应用名
	 */
	private String appName;
	/**
	 * 应用模块
	 */
	private String appType;

	private Long dtTime;

	/**
	 * 前后台统一TID
	 */
	private String tid;// 前后台统一TID

	private String logLevel;

	/**
	 * 操作内容
	 ***********************/
	private String module;// 操作的模块
	private String operateDesc;// 操作描述

	/**
	 * 操作执行情况
	 ***********************/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	protected Date operateTime; // 操作时间
	private Long costTime;// 操作花费时间
	private int operateResult;// 操作结果:成功/失败
	private String errorMessage;// 失败的error信息

	/**
	 * 系统参数，若持久化到数据库，建议不要保存这些信息
	 ***********************/
	private String className;// 类
	private String methodName;// 方法
	// private Object[] methodParameters;// 参数
	private String methodParameters;// 参数
	private Object methodResult;// 方法执行返回的结果

	/**
	 * 非持久化参数
	 */
	@JSONField(serialize = false, deserialize = false)
	private int saveTag = 0;// 是否save方法，若是的话1：新增；2：更新，其它0

	public BaseLog() {
	}

	public BaseLog(Long id) {
		super(id);
	}

	public BaseLog(Long id, String uuid) {
		super(id, uuid);
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public boolean isAlreadyStored() {
		return alreadyStored;
	}

	public void setAlreadyStored(boolean alreadyStored) {
		this.alreadyStored = alreadyStored;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Long getDtTime() {
		return dtTime;
	}

	public void setDtTime(Long dtTime) {
		this.dtTime = dtTime;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public int getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodParameters() {
		return methodParameters;
	}

	public void setMethodParameters(String methodParameters) {
		this.methodParameters = methodParameters;
	}

	public Object getMethodResult() {
		return methodResult;
	}

	public void setMethodResult(Object methodResult) {
		this.methodResult = methodResult;
	}

	public int getSaveTag() {
		return saveTag;
	}

	public void setSaveTag(int saveTag) {
		this.saveTag = saveTag;
	}

}
