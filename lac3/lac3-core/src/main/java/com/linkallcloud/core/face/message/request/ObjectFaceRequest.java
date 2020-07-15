package com.linkallcloud.core.face.message.request;

public class ObjectFaceRequest<T> extends LoginFaceRequest {
	private static final long serialVersionUID = -5002720941981144999L;

	private T data;

	public ObjectFaceRequest() {
		super();
	}

	public ObjectFaceRequest(String userType, String loginName, Long userId, Long companyId, String companyName) {
		super(userType, loginName, userId, companyId, companyName);
	}

	public ObjectFaceRequest(String userType, String loginName, Long userId) {
		super(userType, loginName, userId);
	}

	public ObjectFaceRequest(String token, String versn) {
		super(token, versn);
	}

	public ObjectFaceRequest(T data) {
		super();
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
