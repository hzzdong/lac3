package com.linkallcloud.core.enums;

public enum Logical implements EnumBase<Integer> {
	AND(0, "AND"), OR(1, "OR");

	private int code;
	private String message;

	Logical(int code, String message) {
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

	public static Logical get(Integer code) {
		for (Logical ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
