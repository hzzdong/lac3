package com.linkallcloud.core.face.message.response;

import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.IResponseFaceMessage;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.ResBundle;

public class FaceResponse extends FaceMessage implements IResponseFaceMessage {
	private static final long serialVersionUID = -1370106611117236705L;

	private String code;
	private String message;

	public FaceResponse() {
		super();
		this.code = "0";
		this.message = "success";
	}

	public FaceResponse(String code, String message, String versn) {
		super(versn);
		this.code = code;
		this.message = message;
	}

	public FaceResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public FaceResponse(String code) {
		super();
		this.code = code;
		if (!Strings.isBlank(code) && !"0".equals(code)) {
			String res = ResBundle.getMessage(code);
			if (!Strings.isBlank(res)) {
				this.message = res;
			}
		}
	}

	public FaceResponse(boolean error, String code, String message) {
		super();
		if (error) {
			this.code = code;
			if (Strings.isBlank(message)) {
				if (!Strings.isBlank(code) && !"0".equals(code)) {
					String res = ResBundle.getMessage(code);
					if (!Strings.isBlank(res)) {
						this.message = res;
					}
				}
			} else {
				this.message = message;
			}
		} else {
			this.code = Exceptions.CODE_SUCCESS;
		}
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
