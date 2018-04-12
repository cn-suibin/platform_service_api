package com.mircoservice.fontservice.api.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpRequestHandler;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

import com.alibaba.druid.util.HexBin;
import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.User;
import com.mircoservice.fontservice.api.service.ApiFontService;
import com.mircoservice.fontservice.api.service.ApiFontServiceImpl;
import com.mircoservice.fontservice.api.service.BlockchainService;
import com.mircoservice.fontservice.api.service.ETHService;
import com.mircoservice.fontservice.api.service.FeedService;
import com.mircoservice.fontservice.api.threadpools.FontSyncCommand;
import com.mircoservice.fontservice.api.util.FdHttpClient;
import com.mircoservice.fontservice.api.util.GsonUtil;
import com.mircoservice.fontservice.api.util.R;
import com.mircoservice.fontservice.api.util.RMessage;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;

import cn.merryyou.blockchain.Wallet;
import cn.merryyou.blockchain.WalletKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/*
 * create by suibin
 * API-FEED
 * 2018-03-19
 */
@Api("API-Blockchain")
@RestController
@RequestMapping("blockchain")
public class BlockChainController {

	@Autowired
	BlockchainService BlockchainService;
	

	
	@ApiOperation(value = "addBlockchain", notes = "")
	@RequestMapping(value = "/addBlockchain", method = RequestMethod.GET)
	@ResponseBody
	public String addBlockchain(
    		@RequestParam("previousname") String previousname,        	
    		@RequestParam("from_user") String from_user,
    		@RequestParam("from_publicKeystr") String from_publicKeystr,
    		@RequestParam("from_privateKeystr") String from_privateKeystr,
    		@RequestParam("to_user") String to_user,
    		@RequestParam("to_publicKeystr") String to_publicKeystr,
    		@RequestParam("to_privateKeystr") String to_privateKeystr,
    		@RequestParam("money") String money
			) throws Exception {

		
		Wallet fromwallet=new Wallet();
		Wallet towallet=new Wallet();
		
		//////////////////////////////////////////////////////////////////
		
    	PublicKey from_publicKey1 = WalletKey.converPublicKey(HexBin.decode(from_publicKeystr));
        PublicKey to_publicKey1 = WalletKey.converPublicKey(HexBin.decode(to_publicKeystr));

        PrivateKey from_privateKey1 = WalletKey.converPrivateKey(HexBin.decode(from_privateKeystr));
        PrivateKey to_privateKey1 = WalletKey.converPrivateKey(HexBin.decode(to_privateKeystr));
        
        fromwallet.publicKey=from_publicKey1;		
        towallet.publicKey=to_publicKey1;
        
        fromwallet.privateKey=from_privateKey1;		
        towallet.privateKey=to_privateKey1;
        
        
		BlockchainService.addBlockchain(previousname,from_user,to_user, fromwallet, towallet, Float.parseFloat(money));
		return null;
	}
	
	@ApiOperation(value = "createKeys", notes = "")
	@RequestMapping(value = "/createKeys", method = RequestMethod.GET)
	@ResponseBody
	public String createKeys(
    		@RequestParam("publicKey") String publicKey,
    		@RequestParam("privateKey") String privateKey
			) throws Exception {
		Wallet wallet=new Wallet();
		wallet.generateKeyPair(true,publicKey,privateKey);

		return "------111--------";
	}

	@ApiOperation(value = "creationblock", notes = "")
	@RequestMapping(value = "/creationblock", method = RequestMethod.GET)
	@ResponseBody
	public String creationblock(
    		@RequestParam("publicKeystr") String publicKeystr,
    		@RequestParam("phone") String phone,//用户唯一标识
    		@RequestParam("blockname") String blockname,//创世块名称
    		@RequestParam("money") String money
			) throws Exception {
		
		
        PublicKey from_publicKey = WalletKey.converPublicKey(HexBin.decode(publicKeystr));
		Wallet who_wallet=new Wallet();
		who_wallet.publicKey=from_publicKey;
        String hash=BlockchainService.creationBlockchain(who_wallet, Float.parseFloat(money),phone,blockname);
        

		return hash;
	}
	
}
