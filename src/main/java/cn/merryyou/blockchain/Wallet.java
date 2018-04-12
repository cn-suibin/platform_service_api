package cn.merryyou.blockchain;

import java.io.Serializable;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 * 
 * 数字钱包
 */
public class Wallet implements Serializable{

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
    
    public  HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet() {
        //generateKeyPair();
    }

    public void generateKeyPair(boolean isave,String publicKeyurl ,String privateKeyurl) {
        try {
        	if (Security.getProvider("BC") == null) { 
        		Security.addProvider(new BouncyCastleProvider()); 
        	} 
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            
            if(isave) {
	            WalletKey.setKey(publicKeyurl,publicKey.getEncoded());
	            WalletKey.setKey(privateKeyurl,privateKey.getEncoded());

            }
            

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    //from wallet
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Transaction.UTXOs.entrySet()){//遍历上一次输出，这里从redis获取。
            TransactionOutput UTXO = item.getValue();
            //便利
            //publicKey 是本次from wallet
            //UTXO是上一次的from wallet
            
            //

            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.找出自己钱包的
                total += UTXO.value ;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey _recipient,float value ) {
        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
