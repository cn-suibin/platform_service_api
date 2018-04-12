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

import com.mircoservice.fontservice.api.service.ApiFontService;
import com.mircoservice.fontservice.api.service.ApiFontServiceImpl;
import com.mircoservice.fontservice.api.threadpools.FontSyncCommand;
import com.mircoservice.fontservice.api.util.FdHttpClient;
import com.mircoservice.fontservice.api.util.GsonUtil;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/*
 * create by suibin
 * API-字体API
 * 2017-10-27
 */
@Api("API-字体")
@RestController
@RequestMapping("api")
public class ApiFontController {

	
	@Autowired
	FeeBaseExMapper feeBaseExMapper;
	
    @Autowired
    FdHttpClient fdHttpClient;
    @Autowired
    RestTemplate restTemplate;
    
    public Logger logger = Logger.getLogger(HttpRequestHandler.class);
	@Autowired
	ApiFontService apiFontService;

	
	@ApiOperation(value = "字体同步pool", notes = "字体同步API，对外")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "query", dataType = "string", name = "jsonparam", value = "字体参数集", required = true) })
	@GetMapping(value = "/syncFontPool")
	public String syncFontPool(@RequestParam("jsonparam") String jsonparam) throws Exception {
		
		/* 线程池方式调用同步逻辑 */
        Future<String> future =new FontSyncCommand("FontSyncCommandPool").queue();
        //get操作不能超过command定义的超时时间,默认:1秒  
        String result = future.get(100, TimeUnit.MILLISECONDS);  
        //System.out.println("result=" + result);  
        //System.out.println("mainThread=" + Thread.currentThread().getName()); 
		return (result);
		//return "--------------";
		
		/* 直接调用同步逻辑
		return (apiFontService.inSyncFont("fdid1112345"));
        */
		
	}
	
	//@ApiOperation(value = "字体同步", notes = "字体同步API，对外")
	//@ApiImplicitParams({ @ApiImplicitParam(paramType = "query", dataType = "string", name = "jsonparam", value = "字体参数集", required = true) })
	//@GetMapping(value = "/syncFont")
	@RequestMapping(value = "/syncFont", method = RequestMethod.POST)
	@ResponseBody
	public String syncFont(@RequestBody RequestPageSearchParams requestPageSearchParams) throws Exception {
	    

		//return "--------------";
		/* 直接调用同步逻辑*/
		//return (apiFontService.inSyncFont("fdid1112345"));
		return apiFontService.inSyncFont(requestPageSearchParams);
		
	}
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello() throws Exception {
	    

		return "------111--------";

	
	}
	
	
	@RequestMapping(value = "/syncFont1", method = RequestMethod.GET)
	@ResponseBody
	public String syncFont1(@RequestParam("jsonparam") String jsonparam) throws Exception {
	    
		return GsonUtil.GsonString(feeBaseExMapper.getDeptList());
		//apiFontService.getSyncFont("fdid1112345");
		//return "------111--------"+jsonparam;

	
	}
	@RequestMapping(value = "/syncFont2", method = RequestMethod.GET)
	@ResponseBody
	public String syncFont2(@RequestParam("jsonparam") String jsonparam) throws Exception {
	    
		//ResponseEntity<String> rs=
		//restTemplate.getForEntity("http://192.168.248.118:10009/base/hello2", String.class);
		//return "--------------"+rs.getBody();

	     return fdHttpClient.syncFont2Feign();



	
	}
	@RequestMapping(value = "/syncFont3", method = RequestMethod.GET)
	@ResponseBody
	public String syncFont3(@RequestParam("jsonparam") String jsonparam) throws Exception {
	    
		ResponseEntity<String> rs=
		restTemplate.getForEntity("http://192.168.248.243:10009/base/hello2", String.class);
		return "--------------"+rs.getBody();



	
	}	
	/**
	 *获取当前jar包所在系统中的目录
	 */
	public File getBaseJarPath() {
	    ApplicationHome home = new ApplicationHome(getClass());
	    File jarFile = home.getSource();
	    return jarFile.getParentFile();
	}
	
    //字体拆分接口
	//
	//
	//http://127.0.0.1:10011/api/fontsplit?outype=1&content=啊哈哈&filename=爆米花体__17_11_41_74513.TTF&pathprefix=
    @ApiOperation(value = "字体拆分接口", notes = "支持输出格式:0-ttf;1-woff;2-eot[举例:http://127.0.0.1:10011/api/fontsplit?outype=1&content=啊哈哈&filename=爆米花体__17_11_41_74513.TTF&pathprefix=]")

	@ApiImplicitParams({ 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "outype", value = "输出格式", required = true), 
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "content", value = "拆分内容", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "filename", value = "字体文件名称,如:aaa.ttf", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "pathprefix", value = "字体本地目录(服务器)", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "cache", value = "字体缓存(1开启0关闭默认关闭)", required = false)
	})

	@RequestMapping(value ="/fontsplit", method = RequestMethod.GET)
    public String fontsplit(
    		@RequestParam("outype") String outype,        	//0-ttf,1-woff,2-eot
    		@RequestParam("content") String content,
    		@RequestParam("filename") String filename,
    		@RequestParam(value="pathprefix",required=false) String pathprefix,
    		@RequestParam(value="cache",required=false) String cache,
    		HttpServletRequest request, HttpServletResponse response){
    	if(outype.equals("")||content.equals("")||filename.equals("")){
    		return "检查参数！";
    	}
    	String Suffix="";
    	switch(outype){
    	case "0":Suffix=".ttf";break;
    	case "1":Suffix=".woff";break;
    	case "2":Suffix=".eot";break;
    	}
    	
        String fileName = filename;
        if (fileName != null) {

        	if(pathprefix==null||pathprefix.equals("")){
        		pathprefix=this.getBaseJarPath().getAbsolutePath();
        	}else{
        		
        		
        	}
        	if(cache==null||cache.equals("")){
        		cache="0";
        	}else{
        		
        		
        	}
        	boolean iscache=false;
        	if(cache.equals("1")){
        		iscache=true;
        	}else {
        		
        	}
        	InputStream fis=apiFontService.subFont(pathprefix,"/"+fileName ,content, Integer.parseInt(outype),iscache);//拆字

            if (fis!=null) {
            	
            	String outname=fileName.split("\\.")[0].toString()+Suffix;
            	//System.out.println(outname);
            	try {
					outname= new String(outname.getBytes("utf-8"), "ISO_8859_1");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.setCharacterEncoding("utf-8");
                response.setHeader("contentType", "text/html;charset=UTF-8");
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" +  outname);// 设置文件名
                byte[] buffer = new byte[1024];
                //FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    //fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
    
}
