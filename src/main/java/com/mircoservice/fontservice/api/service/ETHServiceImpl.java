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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

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
 * API
 * 2017-10-27
 */
@Service
public class ETHServiceImpl implements ETHService {
	
	private static final Logger _log = LoggerFactory.getLogger(ETHServiceImpl.class);
	
    @Value("${eth.url}")  
    private String ethurl;  
	
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

	@Override
	public Web3j connectWallet() {
		// TODO Auto-generated method stub
		Web3j web3 = Web3j.build(new HttpService(ethurl));
		return web3;
	}

	@Override
	public void testeth() {
		// TODO Auto-generated method stub
		
		Web3ClientVersion web3ClientVersion;
		try {
			web3ClientVersion = connectWallet().web3ClientVersion().send();
			String clientVersion = web3ClientVersion.getWeb3ClientVersion();
			System.out.println(clientVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    

}
