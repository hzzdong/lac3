package com.linkallcloud.core.dto;

import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = 3608338254322860125L;

	private String ip;// 操作者的登陆ip
	private Boolean mobile;// 是否移动设备
	private String mobileBrand;// 手机品牌信息
	private String os;// 操作系统
	private String osVersion;// 操作系统版本
	private String browser;// 浏览器
	private String browserVersion;// 浏览器版本
	private String ua;// navigator.userAgent

	public Client() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getMobile() {
		return mobile;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}

	public String getMobileBrand() {
		return mobileBrand;
	}

	public void setMobileBrand(String mobileBrand) {
		this.mobileBrand = mobileBrand;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

}
