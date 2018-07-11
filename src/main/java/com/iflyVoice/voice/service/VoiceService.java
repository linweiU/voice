package com.iflyVoice.voice.service;

import org.springframework.web.multipart.MultipartFile;

import com.iflyVoice.voice.common.Result;



public interface VoiceService {

	public Result getVoice(MultipartFile file) throws Exception;

}
