package com.mircoservice.fontservice.api.util;


public class BusinessException extends RuntimeException {

	private ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getCode() + "|" + errorCode.getMessage());
		this.errorCode = errorCode;
	}
	
	
}
