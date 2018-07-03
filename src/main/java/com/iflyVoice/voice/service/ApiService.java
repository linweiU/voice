package com.iflyVoice.voice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Transactional
public interface ApiService {
	/**
	 * 为多线程使用 
	 * @param url
	 * @param paraMap
	 * @return
	 * @throws IOException
	 * @author weilin5
	 * @create_time 2017年9月25日下午5:55:57
	 */
	public String doPostEachUrl(String url, Map<String, String> paraMap) throws IOException ;
    public String doPost(String url, Map<String, String> paraMap) throws IOException;
    public String doGet(String url) throws Exception;
    public JSONObject doVoicePost(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doVoiceGet(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doVoiceDelete(String url, Map<String, Object> paraMap) throws Exception;
    public JSONObject doVoicePatch(String url, Map<String, Object> paraMap) throws Exception;
    public String doPinePost(String url, String jsonParam) throws IOException;
}
