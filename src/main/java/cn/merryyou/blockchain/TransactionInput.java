package cn.merryyou.blockchain;

import java.io.Serializable;

/**
 * Created on 2018/3/10 0010.
 *
 *	交易输入
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class TransactionInput implements Serializable{
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TransactionOutput UTXO; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
