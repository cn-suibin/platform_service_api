package com.mircoservice.fontservice.api.service;

import java.io.InputStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.SyncFont;
import com.mircoservice.fontservice.api.entity.User;
import com.mircoservice.fontservice.api.util.R;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/*
 * create by suibin
 * API-
 * 2017-10-27
 */
public interface FeedService {
	

	public String login();
	public String unlogin();
	public String publishDt();
	public String delDt();

	public String follow();
	public String get_followlist();

	public String get_followedlist();
	

	public String recommend_dynamic();
    
  
	public String follow_dynamic();
	public String zan();

	public String comment();

	public String addcomment();
	
	public R register(User user);
	
}
