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
import com.mircoservice.fontservice.api.service.ETHService;
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
@Api("API-ETH")
@RestController
@RequestMapping("eth")
public class ETHController {

	@Autowired
	ETHService eTHService;
	

	
	@ApiOperation(value = "test", notes = "")
	@RequestMapping(value = "/testEth", method = RequestMethod.GET)
	@ResponseBody
	public String testEth() throws Exception {
		eTHService.testeth();
		return "------111--------";
	}
}
