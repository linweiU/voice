package com.iflyVoice.voice.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iflyVoice.voice.common.Constance;
import com.iflyVoice.voice.service.ApiService;
import com.iflyVoice.voice.util.ArrayToJsonUtil;
import com.iflyVoice.voice.util.HttpClientUtil;
import com.iflyVoice.voice.util.HttpDeleteWithBody;
import com.iflyVoice.voice.util.SignUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;


@Service("apiService")
@Scope("prototype")
public class ApiServiceImpl implements ApiService {
	@Autowired
	private CloseableHttpClient httpClientObj;
	@Autowired
	private RequestConfig httpClientRequestConfig;

	public static final Logger log = Logger.getLogger(ApiService.class);
	@Value("${http.socketTimeout}")
	private int socketTimeout;
	@Value("${http.connectTimeout}")
	private int connectTimeout;
	@Value("${http.connectionRequestTimeout}")
	private int connectionRequestTimeout;
	
	@Override
	public String doPostEachUrl(String url, Map<String, String> paraMap) throws IOException {

		log.debug("doPostEachUrl Post url " + url + "	paraMap " + paraMap.toString());
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		// 设置请求参数
		RequestConfig requestConfig =RequestConfig.custom().setSocketTimeout(socketTimeout)
		.setConnectTimeout(connectTimeout)
		.setConnectionRequestTimeout(connectionRequestTimeout).build();
		httpPost.setConfig(requestConfig);
		if (paraMap != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String s : paraMap.keySet()) {
				parameters.add(new BasicNameValuePair(s, paraMap.get(s)));
			}
			// 构建一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8"));
			// 将请求实体放入到httpPost中
			httpPost.setEntity(formEntity);
		}
		// 创建httpClient对象
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = HttpClientUtil.getInstance().execute(httpPost);
//			response = detectHttpClientObj.execute(httpPost);
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			log.debug("doPostEachUrl Post Request " + resCode);
			return resCode;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("doPostEachUrl 接口异常：" + e);
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public String doPost(String url, Map<String, String> paraMap) throws IOException {

		log.debug("Post url " + url + "	paraMap " + paraMap.toString());
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		// 设置请求参数
		httpPost.setConfig(httpClientRequestConfig);
		
		if (paraMap != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String s : paraMap.keySet()) {
				parameters.add(new BasicNameValuePair(s, paraMap.get(s)));
			}
			// String aa = parameters.toString();
			// 构建一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8"));
			// 将请求实体放入到httpPost中
			httpPost.setEntity(formEntity);
		}
		// 创建httpClient对象
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClientObj.execute(httpPost);
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			log.debug("Post Request " + resCode);
			return resCode;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public String doGet(String url) throws Exception {

		log.debug("Get url " + url);
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpGet httpget = new HttpGet(url);
			response = httpClientObj.execute(httpget);

			// 获取响应实体
			entity = response.getEntity();
			String resCode = EntityUtils.toString(entity, "utf-8");
			log.debug("Get Request " + resCode);

			// if(resCode.startsWith("0:")){ return null; }

			return resCode;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null){
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	private String VoiceCheck(Map<String, Object> paraMap) throws Exception {
		String apiId = Constance.API_KEY;
		String paras[] = (String[]) paraMap.get("paras");
		String sign = SignUtil.getSign(paras);
		String up = apiId + ":" + sign;
		String encode = Base64.byteArrayToBase64(up.getBytes());
		return encode;
	}

	@Override
	public JSONObject doVoicePost(String url, Map<String, Object> paraMap) throws Exception {
		log.debug("VoicePost url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = VoiceCheck(paraMap);

		CloseableHttpResponse response = null;
		StringEntity entity = ArrayToJsonUtil.toJson(paras);
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Authorization", "Basic " + encode);
			httpPost.setEntity(entity);
			httpPost.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpPost);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("VoicePost Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null){
					response.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doVoiceGet(String url, Map<String, Object> paraMap) throws Exception {
		log.debug("VoiceGet url " + url + "	paraMap " + paraMap.toString());
		String encode = VoiceCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Authorization", "Basic " + encode);
			httpGet.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpGet);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("VoiceGet Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doVoiceDelete(String url, Map<String, Object> paraMap) throws Exception {

		log.debug("VoiceDelete url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = VoiceCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			StringEntity entity = ArrayToJsonUtil.toJson(paras);
			HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);
			httpdelete.setHeader("Authorization", "Basic " + encode);
			httpdelete.setEntity(entity);
			httpdelete.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpdelete);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("VoiceDelete Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	@Override
	public JSONObject doVoicePatch(String url, Map<String, Object> paraMap) throws Exception {

		log.debug("VoicePatch url " + url + "	paraMap " + paraMap.toString());
		String paras[] = (String[]) paraMap.get("paras");
		String encode = VoiceCheck(paraMap);
		CloseableHttpResponse response = null;
		try {
			StringEntity entity = ArrayToJsonUtil.toJson(paras);
			HttpPatch httpPatch = new HttpPatch(url);
			httpPatch.setHeader("Authorization", "Basic " + encode);
			httpPatch.setEntity(entity);
			httpPatch.setConfig(httpClientRequestConfig);
			response = httpClientObj.execute(httpPatch);
			HttpEntity res = response.getEntity();
			String resStr = EntityUtils.toString(res, "utf-8");
			log.debug("VoiceGet Request " + resStr);
			JSONObject strJson = JSONObject.parseObject(resStr);
			return strJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("接口异常：" + e);
			return null;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("接口异常：" + e);
			}
		}
	}

	
	public class StreamTool {  
	    /** 
	     * 从输入流中读取数据 
	     * @param inStream 
	     * @return 
	     * @throws Exception 
	     */  
	    public byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len = inStream.read(buffer)) !=-1 ){  
	            outStream.write(buffer, 0, len);  
	        }  
	        byte[] data = outStream.toByteArray();//网页的二进制数据  
	        outStream.close();  
	        inStream.close();  
	        return data;  
	    }  
	}

	/*
	 *  表单提交post请求，直接转网络io未反序列化
	 *  (non-Javadoc)
	 * @see com.uway.web.service.ApiService#doPinePost(java.lang.String, java.lang.String)
	 */
	@Override
	public String doPinePost(String url1, String jsonParam) throws IOException {
		URL url = new URL(url1);
		String result = null;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");// 提交模式
		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		conn.setDoOutput(true);// 是否输入参数

		// StringBuffer params = new StringBuffer();
		// 表单参数与get形式一样
		byte[] bypes = jsonParam.toString().getBytes();
		OutputStream outStream = conn.getOutputStream();
		outStream.write(bypes);// 输入参数
		InputStream inStream = conn.getInputStream();
		try {
			result = new String(new StreamTool().readInputStream(inStream),
					"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (outStream != null) {
				outStream.close();
			}
		}
		return result;
	}  
	
	

}
