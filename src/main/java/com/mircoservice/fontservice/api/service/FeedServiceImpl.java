package com.mircoservice.fontservice.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.SyncFont;
import com.mircoservice.fontservice.api.entity.User;
import com.mircoservice.fontservice.api.util.BusinessException;
import com.mircoservice.fontservice.api.util.ErrorCode;
import com.mircoservice.fontservice.api.util.ErrorEnum;
import com.mircoservice.fontservice.api.util.R;
import com.mircoservice.fontservice.api.util.RedisUtil;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;


import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.tools.conversion.eot.EOTWriter;
import com.google.typography.font.tools.conversion.woff.WoffWriter;
import com.google.typography.font.tools.sfnttool.GlyphCoverage;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import com.google.typography.font.tools.subsetter.Subsetter;
/*
 * create by suibin
 * FEED
 * 2017-10-27
 */
@Service
public class FeedServiceImpl implements FeedService {
	
	private static final Logger _log = LoggerFactory.getLogger(FeedServiceImpl.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	//////////////////////////////////////////

    public  byte[] serializestring(String key) {
    	
		return redisTemplate.getStringSerializer().serialize(key);
    }

    public  String deserializestring(byte[] key) {
    	
		return redisTemplate.getStringSerializer().deserialize(key);
    }
    public  void test() {
    	
		
    }
    
    /*
     * redis超时锁
     *
     *
     * */    
    public  String acquire_lock_with_timeout(RedisConnection conn,String lockname,long acquire_timeout,long lock_timeout) {
    	

   
    	String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    	
    	lockname="lock:"+lockname;
    	long endtime=System.nanoTime()+acquire_timeout;//接口超时时间
    	while(System.nanoTime()<endtime) {
            if(conn.setNX(serializestring(lockname), serializestring(uuid))){
            	conn.expire(serializestring(lockname),lock_timeout);
            	
                return uuid;
          
            }else if(conn.ttl(serializestring(lockname))==-1){
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
    public  boolean release_lock(RedisConnection conn,String lockname,String uuid) {

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
    
	
	//////////////////////////////////////////
	
	@Override
	public String login() {
		// TODO Auto-generated method stub

	    //HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
	    //管道逻辑，性能稳定在一定数值8000。读取时性能低16000。写入相对时性能高8000。
	    
	   
	    List<Object> pwdLogList=redisTemplate.executePipelined(new RedisCallback<List<Object>>() {    
	        @Override    
	        public List<Object> doInRedis(RedisConnection conn)    
	                throws DataAccessException {    
	               
	            conn.exists(redisTemplate.getStringSerializer().serialize("aaa"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("bbb"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
	            conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));

	            return null;    
	        }    
	    }, redisTemplate.getStringSerializer()); 
	    System.out.println("-------total-------:"+pwdLogList.size());
	    for(Object item : pwdLogList){
	        System.out.println("--------------:"+item.toString());
	     }     

		
		return null;
	}
	@Override
	public String unlogin() {
		// TODO Auto-generated method stub
    	RedisConnection conn=redisTemplate.getConnectionFactory().getConnection();
    	//conn.watch(serializestring("aaa"));

		String uuid=RedisUtil.acquire_lock_with_timeout(conn,"test111", 10, 10);
    	System.out.println("======================");

        conn.set(redisTemplate.getStringSerializer().serialize("aaa"), redisTemplate.getStringSerializer().serialize("aaa"));

		//System.out.println("======================"+uuid);
		RedisUtil.release_lock(conn,"test111",uuid);
        
		conn.close();
		return null;
	}
	@Override
	public String publishDt() {
		// TODO Auto-generated method stub
		//redisTemplate下面方式跟管道性能一样！有继承关系时，管道无效。
    	RedisConnection conn=redisTemplate.getConnectionFactory().getConnection();
    	conn.openPipeline();
    	//.multi();
        conn.exists(redisTemplate.getStringSerializer().serialize("aaa"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("bbb"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.hGetAll(redisTemplate.getStringSerializer().serialize("OriginalFontBo"));
        conn.closePipeline();
        //conn.exec();
        //conn.setNX(serializestring("fdsfsdfd1"), serializestring("6465"));
        //conn.setNX(serializestring("fdsfsdfd2"), serializestring("6465"));
        //conn.setNX(serializestring("fdsfsdfd3"), serializestring("6465"));
        //conn.setNX(serializestring("fdsfsdfd4"), serializestring("6465"));
        //conn.setNX(serializestring("fdsfsdfd5"), serializestring("6465"));
        //conn.setNX(serializestring("fdsfsdfd6"), serializestring("6465"));
    	conn.close();
		return null;
	}
	@Override
	public String delDt() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String follow() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String get_followlist() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String get_followedlist() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String recommend_dynamic() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String follow_dynamic() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String zan() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String comment() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String addcomment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R register(User user) {
		// TODO Auto-generated method stub
		String username=user.getUsername();
		String email=user.getEmail();
		String phone=user.getPhone();
		String password=user.getPassword();
		String cpassword=user.getCpassword();
		
		if(!password.equals(cpassword)) return R.error(416,"fail","俩次密码不一致"); 
		
    	RedisConnection conn=redisTemplate.getConnectionFactory().getConnection();

    	if(conn.hExists(serializestring("user:hash_check_phone"), serializestring(phone))) {
    		conn.close();
    		return R.error(416,"fail","该手机号已经注册");
    	}

    	if(conn.hExists(serializestring("user:hash_check_email"), serializestring(email))) {
    		conn.close();
    		return R.error(416,"fail","该邮箱已经注册");
    	}
    	
    	if(conn.hExists(serializestring("user:hash_check_username"), serializestring(username))) {
    		conn.close();
    		return R.error(416,"fail","该用户名已经注册");
    	}
    	
    	Long userid=conn.incr(serializestring("user:id"));

    	Map<byte[],byte[]> hashes=new HashMap();
    	//conn.watch(serializestring("zhangsan"));
    	conn.multi();//开启事务
    	conn.hSet(serializestring("user:hash_check_phone"), serializestring(phone), serializestring(""+userid));//插入手机号注册
    	conn.hSet(serializestring("user:hash_check_email"), serializestring(email), serializestring(""+userid));//插入邮箱注册
    	conn.hSet(serializestring("user:hash_check_username"), serializestring(username), serializestring(""+userid));//插入用户名注册
    	
    	//注册信息
    	hashes.put(serializestring("username"), serializestring(username));
    	hashes.put(serializestring("email"), serializestring(email));
    	hashes.put(serializestring("phone"), serializestring(phone));
    	
    	//注册信息入库
    	conn.hMSet(serializestring("user:hash_user_"+userid), hashes);
    	//插入用户有序列表，用于管理
    	conn.zAdd(serializestring("user:zset_userlist"), System.nanoTime(),serializestring(userid+""));//用户列表
    	
    	List rs=conn.exec();
        
		conn.close();
		if(rs==null) return null;
		return R.ok(phone);
	}

	
	
}
