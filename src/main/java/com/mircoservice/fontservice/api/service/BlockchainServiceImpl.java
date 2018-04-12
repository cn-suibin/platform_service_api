package com.mircoservice.fontservice.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.mircoservice.fontservice.api.util.GsonUtil;
import com.mircoservice.fontservice.api.util.R;
import com.mircoservice.fontservice.api.util.RedisUtil;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;

import cn.merryyou.blockchain.Block;
import cn.merryyou.blockchain.Transaction;
import cn.merryyou.blockchain.TransactionInput;
import cn.merryyou.blockchain.TransactionOutput;
import cn.merryyou.blockchain.Wallet;
import cn.merryyou.blockchain.WalletKey;
import cn.merryyou.blockchain.utils.JsonUtil;

import com.alibaba.druid.util.HexBin;
import com.google.gson.internal.LinkedTreeMap;
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
public class BlockchainServiceImpl implements BlockchainService {
	
	private static final Logger _log = LoggerFactory.getLogger(BlockchainServiceImpl.class);
	
	
    public static int difficulty = 1;//挖矿难度，POW算法的权值。。N个前导零构成，零的个数取决于网络的难度值。
    public static ArrayList<Block> blockchain = new ArrayList<Block>();//一个区块链
    public static HashMap<String,TransactionOutput> UTXOs =Transaction.UTXOs;
    public static Transaction genesisTransaction;
    
    @Value("${eth.url}")  
    private String ethurl;  
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	//////////////////////////////////////////

	
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.

        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));       	



        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            //延迟时候，可能计算的不一样。
            //if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
            //    System.out.println("#Current Hashes not equal");
            //    return false;
            //}
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        

        return true;
    }
	
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
	//////////////////////////////////////////

    public  byte[] serializestring(String key) {
    	
		return redisTemplate.getStringSerializer().serialize(key);
    }

    public  String deserializestring(byte[] key) {
    	
		return redisTemplate.getStringSerializer().deserialize(key);
    }
    public  void test() {
    	
		
    }

  //序列化 
    public static byte [] serialize(Object obj){
        ObjectOutputStream obi=null;
        ByteArrayOutputStream bai=null;
        try {
            bai=new ByteArrayOutputStream();
            obi=new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt=bai.toByteArray();
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //反序列化
    public static Object unserizlize(byte[] byt){
        ObjectInputStream oii=null;
        ByteArrayInputStream bis=null;
        bis=new ByteArrayInputStream(byt);
        try {
            oii=new ObjectInputStream(bis);
            Object obj=oii.readObject();
            return obj;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return null;
    }
    
	@Override
	public void testeth() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public String addBlockchain(String previousname,String from_user,String to_user,Wallet fromwallet,Wallet towallet, float value) {

		
		
		PublicKey ppp33=fromwallet.publicKey;
		PrivateKey ppp=fromwallet.privateKey;
		// TODO Auto-generated method stub
		String previousHash=null;
    	RedisConnection conn=redisTemplate.getConnectionFactory().getConnection();
    	//if(conn.exists(serializestring(previousname))) {
    		
    		//	    	conn.set(serializestring(phone+":"+blockname+"_blockchain"), serialize(blockchain));
	    	//conn.set(serializestring(phone+":"+blockname+"_UXTOS"), serialize(UTXOs));//
    		

    		byte[] xx=conn.get(serializestring(previousname+"_blockchain"));
    		byte[] xx1=conn.get(serializestring(previousname+"_UXTOS"));
    		byte[] xx2=conn.get(serializestring(previousname+"_firstTR"));
    		byte[] xx3=conn.get(serializestring(previousname+"_wallet"));
    		//System.out.println(xx);
    		//System.out.println(xx1);
    		blockchain=(ArrayList<Block>) unserizlize(xx);
    		UTXOs=(HashMap<String, TransactionOutput>) unserizlize(xx1);//output输出函数,根据上一次的计算
    
    		genesisTransaction=(Transaction) unserizlize(xx2);
    		fromwallet= (Wallet) unserizlize(xx3);
    		fromwallet.setPublicKey(ppp33);
    		fromwallet.privateKey=ppp;
    		//fromwallet.UTXOs=UTXOs;
    		Transaction.UTXOs=UTXOs;//加载交易情况
    		
    		previousHash=blockchain.get(blockchain.size()-1).hash;
//    		String json=deserializestring(xx);
//    		blockchain=(ArrayList<Block>) JsonUtil.toBean(json);
//    		ArrayList<LinkedTreeMap>  jsontoBean=(ArrayList<LinkedTreeMap>) GsonUtil.GsonToList(json, LinkedTreeMap.class);
//    		LinkedTreeMap b=jsontoBean.get(jsontoBean.size()-1);
//    		Object xzzz=b.get("hash");

//    		previousHash=(String) xzzz;
    	//}
		if(previousHash==null) {

			return null;
		}
        //System.out.println("--------from钱包公钥----------:"+new String(fromwallet.publicKey.getEncoded()));
        //System.out.println("---------to钱包公钥---------:"+new String(towallet.publicKey.getEncoded()));
		
        Block block = new Block(previousHash);
        Transaction tr= fromwallet.sendFunds(towallet.publicKey, value);//from发送到to钱包
        if(tr!=null) {
            block.addTransaction(tr);//发送给XXX消息
            addBlock(block); 
        }
   
        
        if(!isChainValid()) {
            System.out.println(JsonUtil.toJson(blockchain));//返回区块链账单
    		conn.close();
        	return null;
        }

    	//conn.multi();//开启事务
    	//conn.set(serializestring(phone+":"+blockname), serializestring(JsonUtil.toJson(blockchain)));//插入手机号注册
    	conn.set(serializestring(previousname+"_blockchain"), serialize(blockchain));
    	conn.set(serializestring(previousname+"_UXTOS"), serialize(UTXOs));//

    	
    	
    	//conn.exec();
       // conn.set(serializestring(previousname), serializestring(JsonUtil.toJson(blockchain));
        conn.close();
        System.out.println(from_user+"的钱包余额："+fromwallet.getBalance());
        System.out.println(to_user+"的钱包余额："+towallet.getBalance());
        System.out.println(JsonUtil.toJson(blockchain));//返回区块链账单
		return block.hash;
	}

	@Override
	public String creationBlockchain(Wallet who_wallet, float value,String phone,String blockname) {
		// TODO Auto-generated method stub
		//blockchain.clear();
    	RedisConnection conn=redisTemplate.getConnectionFactory().getConnection();
    	if(conn.exists(serializestring(phone+":"+blockname))) {
    		
    		
    		byte[] xx=conn.get(serializestring(phone+":"+blockname));
    		String json=deserializestring(xx);
    		ArrayList<LinkedTreeMap>  jsontoBean=(ArrayList<LinkedTreeMap>) GsonUtil.GsonToList(json, LinkedTreeMap.class);
    		LinkedTreeMap b=jsontoBean.get(0);
    		Object xzzz=b.get("hash");
    		conn.close();
    		return (String) xzzz;
    	}
		
        Wallet coinbase = new Wallet();
        coinbase.generateKeyPair(false, null, null);
        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, who_wallet.publicKey, value, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);
        //if(!isChainValid()) {
            //System.out.println(JsonUtil.toJson(blockchain));//返回区块链账单
        //	return null;
        //}
        

    	System.out.println(blockchain.size());
        {
    	
	    	conn.multi();//开启事务
	    	//conn.set(serializestring(phone+":"+blockname), serializestring(JsonUtil.toJson(blockchain)));//插入手机号注册
	    	conn.set(serializestring(phone+":"+blockname+"_blockchain"), serialize(blockchain));
	    	conn.set(serializestring(phone+":"+blockname+"_UXTOS"), serialize(UTXOs));//
	    	conn.set(serializestring(phone+":"+blockname+"_firstTR"), serialize(genesisTransaction));//
	    	conn.set(serializestring(phone+":"+blockname+"_wallet"), serialize(who_wallet));//	    	
	    	
	    	conn.set(serializestring(phone+":"+blockname+"_json"), serializestring(JsonUtil.toJson(blockchain)));//	 
	    	
	    	conn.exec();
    	}
		conn.close();
		blockchain.clear();
        
		System.out.println("钱包A余额："+who_wallet.getBalance());
		/*
		Wallet fromwallet=new Wallet();
		Wallet towallet=new Wallet();
		
		//////////////////////////////////////////////////////////////////
		
    	PublicKey from_publicKey1 = null;
		try {
			from_publicKey1 = WalletKey.converPublicKey(HexBin.decode("3049301306072A8648CE3D020106082A8648CE3D03010103320004666C68B1103759B4FBAD2F049E8908F713F787599914DC2A5273E9AE20C2F51BC576152E000A93AD04FE9252DBF91C9D"));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        PublicKey to_publicKey1 = null;
		try {
			to_publicKey1 = WalletKey.converPublicKey(HexBin.decode("3049301306072A8648CE3D020106082A8648CE3D030101033200045E731667ED4546A895C636AE8D5D92E5674B4E235700CA9ECE7C2CDADD03239BDCE69BEA5059F0A8C708D79C72C901F4"));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        PrivateKey from_privateKey1 = null;
		try {
			from_privateKey1 = WalletKey.converPrivateKey(HexBin.decode("307B020100301306072A8648CE3D020106082A8648CE3D0301010461305F0201010418342B832746F634B4F38504EAB486FCD626A1941315334C9DA00A06082A8648CE3D030101A13403320004666C68B1103759B4FBAD2F049E8908F713F787599914DC2A5273E9AE20C2F51BC576152E000A93AD04FE9252DBF91C9D"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //PrivateKey to_privateKey1 = WalletKey.converPrivateKey(WalletKey.loadKey("D:\\KEYS\\157595075.PRI"));
        
        fromwallet.publicKey=from_publicKey1;		
        towallet.publicKey=to_publicKey1;
        
        fromwallet.privateKey=from_privateKey1;		
        //towallet.privateKey=to_privateKey1;
        //fromwallet.UTXOs=UTXOs;
        Block block = new Block(genesis.hash);
        who_wallet.privateKey=from_privateKey1;
        block.addTransaction(who_wallet.sendFunds(towallet.publicKey, 100));//发送给XXX消息
        addBlock(block);
        
        Block block1 = new Block(block.hash);
        who_wallet.privateKey=from_privateKey1;
        block1.addTransaction(who_wallet.sendFunds(towallet.publicKey, 200));//发送给XXX消息
        addBlock(block1); 
        
        
        if(!isChainValid()) {
            //System.out.println(JsonUtil.toJson(blockchain));//返回区块链账单
    		conn.close();
        	return null;
        }
		System.out.println("钱包A余额："+who_wallet.getBalance());
		System.out.println("钱包B余额："+towallet.getBalance());
		*/
        System.out.println(JsonUtil.toJson(blockchain));//返回区块链账单
		return genesis.hash;
	}
    

	
	
}
