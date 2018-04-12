package com.mircoservice.fontservice.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * GSON工具类
 *
 * @author suibin
 * @create 2017-11-01 11:25
 **/

public class RedisUtil {



	private static StringRedisTemplate redisTemplate=null;
	
    static {
        if (redisTemplate == null) {
        	redisTemplate = new StringRedisTemplate();
        }
    }
    public static byte[] serializestring(String key) {
    	
		return redisTemplate.getStringSerializer().serialize(key);
    }

    public static String deserializestring(byte[] key) {
    	
		return redisTemplate.getStringSerializer().deserialize(key);
    }
    public static void test() {
    	
		
    }
    
    /*
     * redis超时锁（防止并发重复更新）
     *
     *
     * */    
    public static String acquire_lock_with_timeout(RedisConnection conn,String lockname,long acquire_timeout,long lock_timeout) {
    	

   
    	String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    	
    	lockname="lock:"+lockname;
    	long endtime=System.nanoTime()+acquire_timeout;//接口超时时间
    	while(System.nanoTime()<endtime) {
            if(conn.setNX(serializestring(lockname), serializestring(uuid))){//若给定的 key 已经存在，则 SETNX 不做任何动作。
            	conn.expire(serializestring(lockname),lock_timeout);
            	
                return uuid;
          
            }else if(conn.ttl(serializestring(lockname))==-1){//如果添加失败，更新时间
            	conn.expire(serializestring(lockname),lock_timeout);
            }
            try {
				Thread.sleep((long) 0.001);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	return null;
    	
    }
    /*
     * redis释放锁
     *
     *
     * */
    public static boolean release_lock(RedisConnection conn,String lockname,String uuid) {

    	lockname="lock:"+lockname;
        //while (true){
            try {
            	conn.watch(serializestring(lockname));
            	byte[] xxx=conn.get(serializestring(lockname));
               if(deserializestring(xxx).equals(uuid)){
            	   try {
            	   conn.openPipeline();
            	   conn.del(serializestring(lockname));
            	   conn.closePipeline();
            	   return true;
	               }finally{
	            	   //RedisConnectionUtils.releaseConnection(conn, redisTemplate.getConnectionFactory());
	               }
               }
               conn.unwatch();
               //break;

            } catch (Exception $ex) {
            	
                //continue;
            }
        //}
		return false;
    }    
    
    
   
}
