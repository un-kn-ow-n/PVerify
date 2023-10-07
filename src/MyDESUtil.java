import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class MyDESUtil {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	 public static String encrypt(String message, String psw, byte[] iv) { 
		 try{   
			 DESKeySpec desKey = new DESKeySpec(psw.getBytes());  
			 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
			 SecretKey securekey = keyFactory.generateSecret(desKey);  
			 Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding"); 
			 cipher.init(Cipher.ENCRYPT_MODE, securekey, new IvParameterSpec(iv));
			 return new String(Base64.getEncoder().encode(cipher.doFinal(message.getBytes("utf-8"))));
		 }catch(Exception e){
			 throw new RuntimeException(e);
		 }
	 }
	 
	 public static String decrypt(String c, String psw, byte[] iv) throws Exception { 
			DESKeySpec desKey = new DESKeySpec(psw.getBytes());  
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
		 	cipher.init(Cipher.DECRYPT_MODE, securekey,new IvParameterSpec(iv));
		 	return new String(cipher.doFinal(Base64.getDecoder().decode(c)));  
	 }
	 
	 public static void main(String[] args) throws Exception {
		 byte[] iv = {30,30,30,30,30,30,30,30};
		 String hello = "e4ab";
		 String key = "21345678sdstgsdfs";
		 System.out.println(key.getBytes()[1]);
		 System.out.println(encrypt(hello, key, iv));
		 System.out.println(decrypt(encrypt("pXBa3/I=", key, iv), key, iv));
	}
}
