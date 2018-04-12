package com.mircoservice.fontservice.api.util;
/**
 * 提示错误信息
 * @author XiRuiQiang
 *
 */
public enum RMessage {
	SUCCESS(0, "成功"), 
	USER_NOT(100, "用户不存在"), 
	LOGIN_ERROR(101, "登录失败"),
	CAPTCHA_ERROR(102,"验证码错误"),
	USERNAME_PASSWORD_ERROR(103,"用户名或密码错误"),
	UPDATE_PASSWORD_ERROR(104,"修改密码失败");
	private Integer code;
	private String msg;

	private RMessage(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
