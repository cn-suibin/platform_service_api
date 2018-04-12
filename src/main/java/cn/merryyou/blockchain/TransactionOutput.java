package cn.merryyou.blockchain;

import cn.merryyou.blockchain.utils.StringUtil;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created on 2018/3/10 0010.
 *
 * 交易输出
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class TransactionOutput implements Serializable{

    public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
    	
        //System.out.println("--------当前钱包公钥----------:"+new String(publicKey.getEncoded()));
        //System.out.println("--------之前钱包公钥----------:"+new String(reciepient.getEncoded()));
        
        
    	//收到的
        return new String(publicKey.getEncoded()).equals(new String(reciepient.getEncoded()));
    }
}
