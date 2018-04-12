/*
 * by suibin
 * 线程池-字体同步池
 * 
 */

package com.mircoservice.fontservice.api.threadpools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mircoservice.fontservice.api.service.ApiFontService;
import com.mircoservice.fontservice.api.service.ApiFontServiceImpl;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class FontSyncCommand extends HystrixCommand<String>{
	
	
	
	private final String name;  
	//初始化，创建线程池
    public FontSyncCommand(String name){
    	
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ThreadPoolFontGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("FontSyncCommandKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(name))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(5000)
                )
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withMaxQueueSize(100)   //配置队列大小
                                .withCoreSize(10)    // 配置线程池里的线程数
                )
        );
        this.name = name; 
    }

    @Override
    protected String run() throws Exception {
    	//调用同步字体接口逻辑
    	ApiFontService apiFontService=new ApiFontServiceImpl();
    	String result="";
    	//String result=apiFontService.inSyncFont("fdid1112345");
        return result;
    }
    //@Override  
    //protected String getFallback() { 
    //	System.out.println("-----exeucute Falled-------");
    //    return "exeucute Falled";  
    //}  
    /*
     * (non-Javadoc)
     * @查询缓存
     */
/*    @Override  
    protected String getCacheKey() {  
        return String.valueOf(name);  
    }  */
    public static class UnitTest {
        @Test
        public void testGetOrder() throws Exception, ExecutionException, TimeoutException{
//            new FontSyncCommand("FontSyncCommandPool").execute();
        	
        	//异步调用
            Future<String> future =new FontSyncCommand("FontSyncCommandPool").queue();

            //get操作不能超过command定义的超时时间,默认:1秒  
            String result = future.get(100, TimeUnit.MILLISECONDS);  
            System.out.println("result=" + result);  
            //System.out.println("mainThread=" + Thread.currentThread().getName()); 
    	
        }

    }


}
