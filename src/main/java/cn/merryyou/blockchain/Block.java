package cn.merryyou.blockchain;

import cn.merryyou.blockchain.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created on 2018/3/10 0010.
 * 区块类
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class Block implements Serializable{
    public String hash;//返回当前块HASH
    public String previousHash;//上一个块HASH
    public String merkleRoot;//默克尔树HASH
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
    public long timeStamp; //as number of milliseconds since 1/1/1970.时间戳
    public int nonce;//工作量证明
    

    //Block Constructor.
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    //区块hash值生成,根据下面4个属性计算获得
    //1、前一个hash
    //2、时间戳
    //3、工作量
    //4、默克尔树hash
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    //difficulty:POW算法的权值。。N个前导零构成，零的个数取决于网络的难度值。
    //
    public void mineBlock(int difficulty) {
    	//默克尔树
        merkleRoot = StringUtil.getMerkleRoot(transactions);//merkle tree 
        //挖矿，工作量证明。【共识算法（POW)】
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((previousHash != "0")) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
