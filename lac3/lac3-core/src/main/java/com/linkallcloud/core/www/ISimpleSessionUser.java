package com.linkallcloud.core.www;

import java.io.Serializable;

import com.linkallcloud.core.dto.Sid;

public interface ISimpleSessionUser extends Serializable {

	static String SESSION_USER_KEY = "_LAC_SESSION_USER_KEY_";

	Sid getSid();
	void setSid(Sid id);

	String getLoginName();
	void setLoginName(String loginName);

	boolean isAdmin();
	void setAdmin(boolean admin);

	String getUserType();
	void setUserType(String userType);

	Sid getCompany();
	Sid getOrg();
	Sid getApp();

	Integer getLoginMode();
	ISimpleSessionUser getSrcUser();

}
