package com.iflyVoice.voice.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.iflyVoice.voice.common.Constance;

public class TestWebIat {
	

	/**
	 * 组装http请求头
	 * 
	 * @param aue
	 * @param engineType
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	public static Map<String, String> constructHeader(String aue, String engineType) throws UnsupportedEncodingException, ParseException {
		// 系统当前时间戳
		String X_CurTime = System.currentTimeMillis() / 1000L + "";
		// 业务参数
		String param = "{\"aue\":\""+aue+"\""+",\"engine_type\":\"" + engineType + "\"}";
		String X_Param = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
		// 接口密钥
		String apiKey = Constance.TEST_API_KEY;
		// 讯飞开放平台应用ID
		String X_Appid = Constance.TEST_APPID;
		// 生成令牌
		String X_CheckSum = DigestUtils.md5Hex(apiKey + X_CurTime + X_Param);

		// 组装请求头
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", X_Param);
		header.put("X-CurTime", X_CurTime);
		header.put("X-CheckSum", X_CheckSum);
		header.put("X-Appid", X_Appid);
		return header;
	}

	public static void main(String[] args) throws IOException,ParseException{
		Map<String, String> header = constructHeader("raw", "sms16k");
		// 读取音频文件，转二进制数组，然后Base64编码
		byte[] audioByteArray = FileUtil.read2ByteArray(Constance.AUDIO_FILE_PATH);
		String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
		String bodyParam = "audio=" + audioBase64;
		String result = HttpUtil.doPost(Constance.WEBIAT_URL, header, bodyParam);
		System.out.println(result);
	}
}
