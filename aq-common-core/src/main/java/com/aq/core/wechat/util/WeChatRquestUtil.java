package com.aq.core.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.util.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
/**
 * 
 * @ClassName: WeChatRquestUtil
 * @Description: 微信请求util
 * @author: lijie
 * @date: 2018年3月15日 上午10:55:59
 */
@Slf4j
public class WeChatRquestUtil {

	
	/**
	 * @throws Exception 
	* @Title: sendWeiChatPostJSON  
	* @Description: 针对微信 post 发送，token失效会刷新一次token 
	* @param: @param url
	* @param: @param data
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	public static String sendPostJsonByToken(final String url, final String data) throws Exception {
		return sendPostJson(url, data, WeChatSignatureUtil.getAccessToken());
	}
	/**
	 * 
	* @Title: sendPostJsonByToken  
	* @Description: TODO 
	* @param: @param url
	* @param: @param data
	* @param: @param token
	* @param: @return
	* @param: @throws Exception
	* @return String
	* @author lijie
	* @throws
	 */
	public static String sendPostJsonByToken(final String url, final String data,final String token) throws Exception {
		
		return sendPostJson(url, data, token);
	}
	/**
	 * 
	* @Title: sendPostJson  
	* @Description: TODO 
	* @param: @param url
	* @param: @param data
	* @param: @param token
	* @param: @return
	* @param: @throws Exception
	* @return String
	* @author lijie
	* @throws
	 */
	private static String sendPostJson(final String url, final String data, final String token) throws Exception {
		String sendUrl = url + "?access_token=" + token;
		String response = HttpClientUtils.sendPostJsonByDomin(sendUrl, data);
		log.info("微信带token post请求响应数据={}", response);
		JSONObject jsonObject = JSON.parseObject(response);
		String errCode = jsonObject.getString("errcode");
		if (WheChatConstant.ACCESSTOKEN_INVALID.equals(errCode)) {
			sendUrl = url + "?access_token=" + WeChatSignatureUtil.refreshToken(token);
			response = HttpClientUtils.sendPostJsonByDomin(sendUrl, data);
			log.info("微信post请求token失效重新刷新token然后请求结果={}", response);
		}
		return response;
	}
	/**
	 * 
	* @Title: senGetByToken  
	* @Description: 针对微信 get 发送，token失效会刷新一次token  
	* @param: @param url
	* @param: @param params
	* @param: @return
	* @param: @throws Exception
	* @return String
	* @author lijie
	* @throws
	 */
	public static String sendGetByToken(final String url, final Map<String, Object> params) throws Exception {
		String token = WeChatSignatureUtil.getAccessToken();
		params.put("access_token", token);
		String response = HttpClientUtils.sendGet(url, params).getResponseContent();
		log.info("微信带token get 请求响应数据={}", response);
		JSONObject jsonObject = JSON.parseObject(response);
		String errCode = jsonObject.getString("errcode");
		if (WheChatConstant.ACCESSTOKEN_INVALID.equals(errCode)) {
			params.put("access_token", WeChatSignatureUtil.refreshToken(token));
			response = HttpClientUtils.sendGet(url, params).getResponseContent();
			log.info("微信get请求token失效重新刷新token然后请求结果={}", response);
		}
		return response;
	}
}
