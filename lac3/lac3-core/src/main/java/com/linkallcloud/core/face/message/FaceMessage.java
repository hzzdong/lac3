package com.linkallcloud.core.face.message;

public abstract class FaceMessage implements IFaceMessage {
	private static final long serialVersionUID = -8652655112962011965L;

	private String versn;// 版本号
	private String appCode;// 应用code

	public FaceMessage() {
		this.versn = "1.0";
	}

	public FaceMessage(String versn) {
		this.versn = versn;
	}

	@Override
	public String getVersn() {
		return versn;
	}

	public void setVersn(String versn) {
		this.versn = versn;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
