package com.linkallcloud.core.face.message.request;

public class PasswordFaceRequest extends LoginFaceRequest {
	private static final long serialVersionUID = 3700931917038907724L;

	private String oldpass;
	private String password;

	public String getOldpass() {
		return oldpass;
	}

	public void setOldpass(String oldpass) {
		this.oldpass = oldpass;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
