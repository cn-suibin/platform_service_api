package com.mircoservice.fontservice.api.service;

import java.io.InputStream;
import java.security.PublicKey;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.protocol.Web3j;

import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.SyncFont;
import com.mircoservice.fontservice.api.entity.User;
import com.mircoservice.fontservice.api.util.R;

import cn.merryyou.blockchain.Wallet;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/*
 * create by suibin
 * API-
 * 2017-10-27
 */
public interface BlockchainService {
	
	//上链
	public String addBlockchain(String previousHash,String from_user,String to_user,Wallet fromwallet,Wallet towallet, float value);
	
	//创世块
	public String creationBlockchain(Wallet who_wallet, float value,String phone,String blockname);
	
	public void testeth();
	
}
