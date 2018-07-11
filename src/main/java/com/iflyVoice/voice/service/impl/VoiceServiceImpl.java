package com.iflyVoice.voice.service.impl;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iflyVoice.voice.common.Constance;
import com.iflyVoice.voice.common.Result;
import com.iflyVoice.voice.common.ResultUtil;
import com.iflyVoice.voice.service.VoiceService;
import com.iflyVoice.voice.util.FileUtil;
import com.iflyVoice.voice.util.HttpUtil;
import com.iflyVoice.voice.util.TestWebIat;

@Service
public class VoiceServiceImpl implements VoiceService{

	@Override
	public Result getVoice(MultipartFile file) throws Exception {
		Map<String, String> header = TestWebIat.constructHeader("raw", "sms16k");
		// 读取音频文件，转二进制数组，然后Base64编码
		byte[] audioByteArray = FileUtil.inputStream2ByteArray(file.getInputStream());
		String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
		String bodyParam = "audio=" + audioBase64;
		String result = HttpUtil.doPost(Constance.WEBIAT_URL, header, bodyParam);
		return ResultUtil.getResultSuccess(result);
	}

}
