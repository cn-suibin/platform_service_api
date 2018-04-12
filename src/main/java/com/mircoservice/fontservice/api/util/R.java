package com.mircoservice.fontservice.api.util;

public class R {
	private Integer code;
	private String msg;
	private Object data;

	public R(Integer code, String msg,Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public R() {
		super();
	}

	public R(RMessage msg) {
		this.setCode(msg.getCode());
		this.setMsg(msg.getMsg());
	}

	public static R error(String msg) {
		return new R(500, msg,null);
	}

	public Integer getCode() {
		return code;
	}

	public R setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}
	

	public Object getData() {
		return data;
	}

	public R setData(Object data) {
		this.data = data;
		return this;
	}

	public R setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public static R ok() {
		return new R(RMessage.SUCCESS);
	}
	public static R ok(Object data) {
		return new R(RMessage.SUCCESS).setData(data);
	}

	public static R error(RMessage msg) {
		return new R(msg);
	}

	public static R error(Integer code, String msg,Object data) {
		return new R(code,msg,data);
	}

	@Override
	public String toString() {
		return "R [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
	
	

}
