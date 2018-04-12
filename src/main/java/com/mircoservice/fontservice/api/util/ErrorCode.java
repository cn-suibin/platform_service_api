package com.mircoservice.fontservice.api.util;



import java.io.Serializable;


public class ErrorCode implements Serializable{
	
	public ErrorCode(ErrorEnum e){
		code = e.getCode();
		message = e.getMessage();
	}
	
	public ErrorCode(){
		
	}


	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
