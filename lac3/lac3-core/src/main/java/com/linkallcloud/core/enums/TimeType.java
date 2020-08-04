package com.linkallcloud.core.enums;

public enum TimeType implements EnumBase<Integer> {

	Normal(0, "正常"), ToTimeout(1, "即将超时"), Timeout(2, "超时"), SeriousTimeout(3, "严重超时");

	private Integer code;
	private String message;

	TimeType(Integer code, String message) {
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

	public static TimeType get(Integer code) {
		for (TimeType ul : values()) {
			if (ul.getCode().equals(code)) {
				return ul;
			}
		}
		return null;
	}

}
