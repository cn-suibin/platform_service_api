package cn.merryyou.blockchain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.druid.util.HexBin;
import com.google.gson.Gson;

public class WalletKey {
	
    private static Gson gson = null;
    static {
    	if (Security.getProvider("BC") == null) { 
    		Security.addProvider(new BouncyCastleProvider()); 
    	} 
    }

	public static void setKey(String xx,byte[] z) throws IOException {
        //2.对生成的密钥key进行编码保存  
        String keyencode= HexBin.encode(z);  
        //写入文件保存  
        File file=new File(xx);  
        OutputStream outputStream=new FileOutputStream(file);  
        outputStream.write(keyencode.getBytes());  
        outputStream.close();  
        System.out.println(keyencode+" -----> key保存成功");  
		
	}
	public static byte[] loadKey(String xx) throws IOException {
		
        //1.读取文件中的密钥  
        File file = new File(xx);  
        InputStream inputStream = new FileInputStream(file);//文件内容的字节流  
        InputStreamReader inputStreamReader= new InputStreamReader(inputStream); //得到文件的字符流  
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader); //放入读取缓冲区  
        String readd="";  
        StringBuffer stringBuffer=new StringBuffer();  
        while ((readd=bufferedReader.readLine())!=null) {  
            stringBuffer.append(readd);  
        }  
        inputStream.close();  
        String keystr=stringBuffer.toString();  
        System.out.println(keystr+" -----> key读取成功");  //读取出来的key是编码之后的 要进行转码  
          
     //2.通过读取到的key  获取到key秘钥对象  
        byte[] keybyte= HexBin.decode(keystr);  
        return keybyte;
	}
	
	//
	public static PublicKey converPublicKey(byte[] code) throws NoSuchAlgorithmException, InvalidKeySpecException   {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(code);  
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");  
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);  
		return publicKey;
		
	}
	//
	public static PrivateKey converPrivateKey(byte[] code) throws NoSuchAlgorithmException, InvalidKeySpecException   {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(code);  
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");  
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec); 		
		return privateKey;

		
	}	
	
}
