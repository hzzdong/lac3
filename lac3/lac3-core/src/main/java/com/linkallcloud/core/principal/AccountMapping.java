package com.linkallcloud.core.principal;

import com.linkallcloud.core.enums.EnumBase;

public enum AccountMapping implements EnumBase<Integer> {

	Unified(1, "统一账号"), Mapping(2, "账号映射");

	private Integer code;
	private String message;

	AccountMapping(Integer code, String message) {
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

	public static AccountMapping get(Integer code) {
		for (AccountMapping ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
