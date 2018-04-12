package com.mircoservice.fontservice.api.util;


public enum ErrorEnum {

	//01 参数
	APPKEY_PARAM_NOT_EXIST("01001","缺少 appkey 请求参数"),
	TIME_PARAM_NOT_EXIST("01002", "缺少 time 请求参数"),
	MAC_PARAM_NOT_EXIST("01003", "缺少 mac 请求参数"),
	TIME_PARAM_LIMIT_INNER_15_MINIUTES("01004", "参数 time 与当前时间相差超过15分钟"),
	ACCESS_TOKEN_PARAM_NOT_EXIST("01005", "缺少 accessToken 请求参数"),
	ID_PARAM_NOT_EXIST("01006", "缺少 id 请求参数"),
	PARTNERUSERID_PARAM_NOT_EXIST("01007", "缺少 partnerUserId 请求参数"),
	SUBSETSTRING_PARAM_NOT_EXIST("01008", "缺少 subsetString 请求参数"),
	
	//02 app
	APP_NOT_EXIST("02001","参数 appkey 对应的APP不存在"),
	MAC_NOT_MATCH("02002", "参数 mac 不匹配，app_secret非法"),
	
	//03 access token
	ILEGAL_ACCESS_TOKEN("03001", "非法accessToken"),
	
	//04 font
	FONT_NOT_EXIST("04001", "参数 id 对应的字体不存在"),
	
	//05 AppFont
	APP_FONT_NOT_EXIST("05001", "字体与App尚未绑定"),
	
	//06 DevelopFont
	DEVELOPER_FONT_NOT_EXIST("06001", "开发者未绑定该款字体"),
	
	//99 系统异常
	SYSTEM_ERROR("99999", "系统异常，请联系管理员");
	
	private ErrorEnum(String code, String message){
		this.code = code;
		this.message = message;
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
