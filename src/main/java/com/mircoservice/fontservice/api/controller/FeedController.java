package com.mircoservice.fontservice.api.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpRequestHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.User;
import com.mircoservice.fontservice.api.service.ApiFontService;
import com.mircoservice.fontservice.api.service.ApiFontServiceImpl;
import com.mircoservice.fontservice.api.service.FeedService;
import com.mircoservice.fontservice.api.threadpools.FontSyncCommand;
import com.mircoservice.fontservice.api.util.FdHttpClient;
import com.mircoservice.fontservice.api.util.GsonUtil;
import com.mircoservice.fontservice.api.util.R;
import com.mircoservice.fontservice.api.util.RMessage;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/*
 * create by suibin
 * API-FEED
 * 2018-03-19
 */
@Api("API-FEED")
@RestController
@RequestMapping("feed")
public class FeedController {

	@Autowired
	FeedService feedService;
	
	
	@ApiOperation(value = "用户登录", notes = "")	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String login() throws Exception {
		
		feedService.login();
		return "------111--------";
	}
	@ApiOperation(value = "用户注销", notes = "")	
	@RequestMapping(value = "/unlogin", method = RequestMethod.GET)
	@ResponseBody
	public String unlogin() throws Exception {
		feedService.unlogin();
		return "------111--------";
	}
	
	@ApiOperation(value = "动态发布", notes = "")
	@RequestMapping(value = "/publishDt", method = RequestMethod.GET)
	@ResponseBody
	public String publishDt() throws Exception {
		feedService.publishDt();
		return "------111--------";
	}

	@ApiOperation(value = "动态删除", notes = "")
	@RequestMapping(value = "/delDt", method = RequestMethod.GET)
	@ResponseBody
	public String delDt() throws Exception {
		return "------111--------";
	}

    @ApiOperation(value = "关注、取消关注", notes = "")
	@ApiImplicitParams({ 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "user_id", value = "输出格式", required = true), 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "target_id", value = "拆分内容", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "state", value = "字体文件名称,如:aaa.ttf", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "pathprefix", value = "字体本地目录(服务器)", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "cache", value = "字体缓存(1开启0关闭默认关闭)", required = false)
	})

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	@ResponseBody
	public String follow() throws Exception {
		return "------111--------";
	}
  
    @ApiOperation(value = "获取当前登录用户关注用户列表接口", notes = "")
	@RequestMapping(value = "/get_followlist", method = RequestMethod.GET)
	@ResponseBody
	public String get_followlist() throws Exception {
		return "------111--------";
	}  
    
    @ApiOperation(value = "获取当前登录用户粉丝用户列表接口", notes = "")
	@RequestMapping(value = "/get_followedlist", method = RequestMethod.GET)
	@ResponseBody
	public String get_followedlist() throws Exception {
		return "------111--------";
	}    
	
    @ApiOperation(value = "首页推荐动态", notes = "")
	@RequestMapping(value = "/recommend_dynamic", method = RequestMethod.GET)
	@ResponseBody
	public String recommend_dynamic() throws Exception {
		return "------111--------";
	}  
    
    @ApiOperation(value = "关注人N动态列表接口", notes = "")
	@RequestMapping(value = "/follow_dynamic", method = RequestMethod.GET)
	@ResponseBody
	public String follow_dynamic() throws Exception {
		return "------111--------";
	}  
    
	@ApiOperation(value = "点赞、取消点赞", notes = "")
	@RequestMapping(value = "/zan", method = RequestMethod.GET)
	@ResponseBody
	public String zan() throws Exception {
		return "------111--------";
	}

	@ApiOperation(value = "获取评论列表pid", notes = "")
	@RequestMapping(value = "/comment", method = RequestMethod.GET)
	@ResponseBody
	public String comment() throws Exception {
		return "------111--------";
	}
	
	@ApiOperation(value = "发表评论", notes = "")
	@RequestMapping(value = "/addcomment", method = RequestMethod.GET)
	@ResponseBody
	public String addcomment() throws Exception {
		return "------111--------";
	}
	@ApiOperation(value = "注册用户", notes = "")
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@ApiImplicitParams({ 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "username", value = "用户名", required = true), 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "phone", value = "手机号", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "email", value = "邮箱", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "password", value = "密码", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "cpassword", value = "确认密码", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "sex", value = "性别", required = true),
	})
	@ResponseBody
	public R register(
			@RequestParam("username") String username,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("cpassword") String cpassword,
			@RequestParam("sex") String sex
			) throws Exception {
		User user=new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPhone(phone);
		user.setPassword(password);
		user.setCpassword(cpassword);
		user.setSex(Integer.parseInt(sex));
		R result=feedService.register(user);
		return result;
	}

	@ApiOperation(value = "点赞人列表（谁给点的赞）", notes = "")
	@RequestMapping(value = "/zan_userlist", method = RequestMethod.GET)
	@ResponseBody
	public String zan_userlist() throws Exception {
		//feedService.register();
		return "------111--------";
	}
	
	@ApiOperation(value = "评论人列表（谁给评论）", notes = "")
	@RequestMapping(value = "/comment_userlist", method = RequestMethod.GET)
	@ResponseBody
	public String comment_userlist() throws Exception {
		//feedService.register();
		return "------111--------";
	}
}
