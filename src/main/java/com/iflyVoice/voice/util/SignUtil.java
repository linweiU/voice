package com.iflyVoice.voice.util;

import com.iflyVoice.voice.common.Constance;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class SignUtil {

	private static final String MAC_NAME = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";     
    /**
     * 得到签名
     * @param strs
     * @return
     * @throws Exception
     */
	public static String getSign(String[] strs) throws Exception{
		String str = sort(strs);
		String sign = bytesToHexString(HmacSHA1Encrypt(str, Constance.API_KEY));
		return sign;
	}
	
	public static long getTime(){  
		Date date = new Date();
	    return date.getTime()/1000;  
	} 
	
	public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
								 
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception     
    {           
        byte[] data=encryptKey.getBytes(ENCODING);  
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称  
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);   
        //生成一个指定 Mac 算法 的 Mac 对象  
        Mac mac = Mac.getInstance(MAC_NAME);   
        //用给定密钥初始化 Mac 对象  
        mac.init(secretKey);    
          
        byte[] text = encryptText.getBytes(ENCODING);    
        //完成 Mac 操作   
        return mac.doFinal(text);
    }

    public static String sort(String[] strs){
    	String newStr = "";
    	Arrays.sort(strs);
    	for(String str : strs) {
    	    newStr += str+"&";
    	}
    	return newStr.substring(0,newStr.length()-1);
    }


}
