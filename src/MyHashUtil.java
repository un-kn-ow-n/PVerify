import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHashUtil {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

	private static String BytetoHexString(byte b) {
		int n = b;
		if(n < 0) {
			n += 256;
		}
		return hexDigits[n / 16] + hexDigits[n % 16];
	}
	
	public static String MyHash(String dataStr, String algorithm){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(dataStr.getBytes("UTF-8"));
            byte[] resultByte = messageDigest.digest();
            String resultStr = "";
            for(int i = 0; i < resultByte.length; i++){
                resultStr += BytetoHexString(resultByte[i]);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
