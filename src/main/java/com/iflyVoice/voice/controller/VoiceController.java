package com.iflyVoice.voice.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.iflyVoice.voice.common.Result;
import com.iflyVoice.voice.common.ResultUtil;
import com.iflyVoice.voice.service.VoiceService;

@RestController
@RequestMapping("voice")
public class VoiceController {

	@Autowired
	private VoiceService voiceService;

	/*
	 * @RequestMapping("/getVoice") public Result
	 * getVoice(@RequestParam("file")MultipartFile file){ if(file == null ||
	 * file.isEmpty()){ return ResultUtil.getResultError("音频文件不得为空！"); } try {
	 * return voiceService.getVoice(file); } catch (Exception e) {
	 * e.printStackTrace(); return ResultUtil.getResultError(e.getMessage()); }
	 * }
	 */

	@RequestMapping(value = "/getVoiceTest")
	public Result getVoice(HttpServletRequest request) {
		// 转型为MultipartHttpRequest(重点的所在)
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得第1张图片（根据前台的name名称得到上传的文件）
		MultipartFile file = null;
		Iterator<String> itr = multipartRequest.getFileNames();
		while (itr.hasNext()) {
			String str = itr.next();
			file = multipartRequest.getFile(str);
			if (file == null || file.isEmpty()) {
				return ResultUtil.getResultError("音频文件不得为空！");
			}
			break;
		}
		try {
			return voiceService.getVoice(file);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.getResultError(e.getMessage());
		}
	}

	@RequestMapping("/test")
	public Result test() {
		return ResultUtil.getResultError("音频文件不得为空！");
	}

}
