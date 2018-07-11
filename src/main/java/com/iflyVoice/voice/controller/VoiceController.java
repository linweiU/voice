package com.iflyVoice.voice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iflyVoice.voice.common.Result;
import com.iflyVoice.voice.common.ResultUtil;
import com.iflyVoice.voice.service.VoiceService;


@RestController
@RequestMapping("voice")
public class VoiceController {
	
	  @Autowired
	  private VoiceService voiceService;
	
	   
	  @RequestMapping("/getVoice")
	   public Result getVoice(@RequestParam("file")MultipartFile file){
		    if(file == null || file.isEmpty()){
		    	return ResultUtil.getResultError("音频文件不得为空！");
		    }
		    try {
				return voiceService.getVoice(file);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultUtil.getResultError(e.getMessage());
			} 
	   }

}
