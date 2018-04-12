package com.mircoservice.fontservice.api.entity;

import java.io.Serializable;
/*
 * create by suibin
 * API-同步
 * 2017-10-27
 */
public class SyncFont implements Serializable{
	

	private static final long serialVersionUID = -2450497743845285899L;
	private Integer fontId;

    public Integer getFontId() {
		return fontId;
	}

	public void setFontId(Integer fontId) {
		this.fontId = fontId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

}


