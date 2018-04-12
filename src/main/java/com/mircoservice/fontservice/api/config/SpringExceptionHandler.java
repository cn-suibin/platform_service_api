package com.mircoservice.fontservice.api.config;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.mircoservice.fontservice.api.util.R;

@ControllerAdvice  
public class SpringExceptionHandler {

	
    /** 
     * 异常页面控制 
     *  
     * @param runtimeException 
     * @return 
     */  
/*    @ExceptionHandler(RuntimeException.class)  
     @ResponseBody  
     public Map<String,Object> runtimeExceptionHandler(RuntimeException runtimeException) {  
        //logger.error(runtimeException.getLocalizedMessage());  
  
        logger.error(runtimeException.getLocalizedMessage());  
        
        Map model = new TreeMap();  
        model.put("status", false);  
        return model;  
    }  */
	
    /** 
     * 全局处理Exception 
     * 错误的情况下返回500 
     * @param ex 
     * @param req 
     * @return 
     */  
	/*
	@ExceptionHandler(RuntimeException.class)  
	@ResponseBody  
    public Object handleOtherExceptions(final Exception ex, final WebRequest req) {  
    	
        String msg = ex.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("message", msg);
        return R.error(msg);

    }  
  
	*/
}
