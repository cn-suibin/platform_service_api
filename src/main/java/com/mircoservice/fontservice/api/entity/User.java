package com.mircoservice.fontservice.api.entity;

import java.io.Serializable;
/*
 * create by suibin
 * API-同步
 * 2017-10-27
 */
public  class User implements Serializable{
	

	private static final long serialVersionUID = -2450497743845285899L;
	
	private Integer userId;
	private String username;
	private String email;
	private String password;
	private String cpassword;
	public String getCpassword() {
		return cpassword;
	}
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
	private String phone;
	private String mylogo;
	private int sex;
	private String startime;
	private String updatetime;
	private int followcount;
	private int followercount;
	private int postcount;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMylogo() {
		return mylogo;
	}
	public void setMylogo(String mylogo) {
		this.mylogo = mylogo;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getStartime() {
		return startime;
	}
	public void setStartime(String startime) {
		this.startime = startime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public int getFollowcount() {
		return followcount;
	}
	public void setFollowcount(int followcount) {
		this.followcount = followcount;
	}
	public int getFollowercount() {
		return followercount;
	}
	public void setFollowercount(int followercount) {
		this.followercount = followercount;
	}
	public int getPostcount() {
		return postcount;
	}
	public void setPostcount(int postcount) {
		this.postcount = postcount;
	}

	
}


