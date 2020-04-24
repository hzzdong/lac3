package com.linkallcloud.core.enums;

public enum LoginMode implements EnumBase<Integer> {
	Normal(0, "正常"), Proxy(1, "代维");

	private int code;
	private String message;

	LoginMode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static LoginMode get(Integer code) {
		for (LoginMode ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
