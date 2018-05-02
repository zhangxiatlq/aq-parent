package com.aq.core.wechat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.aq.core.lock.RedisDistributionLock;
import com.aq.core.rediscache.ICacheService;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.response.SignatureResponse;
import com.aq.core.wechat.response.TokenResponse;
import com.aq.core.wechat.result.SignatureResult;
import com.aq.util.bean.SpringUtil;
import com.aq.util.http.HttpClientUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: WeChatSignature
 * @Description: 得到微信token 及相关签名
 * @author: lijie
 * @date: 2018年1月20日 下午2:20:05
 */
@Slf4j
public class WeChatSignatureUtil {
	
	/**
	 * 最大缓存时间单位秒
	 */
	private static final long MAX_TIME = 7000L;

	private static final String TICKET_CACHE_KEY = "TICKET_KEY";

	private static final String ACCESSTOKEN_CACHE_KEY = "ACCESSTOKEN_KEY";

	@SuppressWarnings("rawtypes")
	private static final ICacheService CACHESERVICE = SpringUtil.getBeanByClass(ICacheService.class);
	/**
	 * 分布式锁
	 */
	private static final RedisDistributionLock DISTRIBUTIONLOCK = SpringUtil.getBeanByClass(RedisDistributionLock.class);
	/**
	 * 
	 * @Title: getSignature
	 * @author: lijie 
	 * @Description: 得到微信签名
	 * @return
	 * @return: SignatureResult
	 */
	public static SignatureResult getSignature(){
		SignatureResult result = new SignatureResult();
		try {
			final String ticket = getTicket(getAccessToken());
			if(StringUtils.isBlank(ticket)){
				return result;
			}
			final String noncestr = UUID.randomUUID().toString();
			final long time = System.currentTimeMillis();
			StringBuilder sbud = new StringBuilder();
			sbud.append("jsapi_ticket=")
			.append(ticket)
			.append("&noncestr=")
			.append(noncestr)
			.append("&timestamp=")
			.append(time/1000)
			.append("&url=")
			.append(WeChatConfig.SIGN_URL);
			String signature = genSha(sbud.toString());
			result.setNonceStr(noncestr);
			result.setSignature(signature);
			result.setTimestamp(String.valueOf(time/1000));
		} catch (Exception e) {
			log.error("get wechat Signature error",e);
		}
		return result;
	}
	/**
	 * 
	 * @Title: getAccessToken
	 * @author: lijie 
	 * @Description: 得到token
	 * @return
	 * @throws Exception
	 * @return: String
	 */
	@SuppressWarnings("unchecked")
	public static String getAccessToken() throws Exception {
		try {
			DISTRIBUTIONLOCK.lock(ACCESSTOKEN_CACHE_KEY);
			String result = (String) CACHESERVICE.get(ACCESSTOKEN_CACHE_KEY);
			if (StringUtils.isBlank(result)) {
				result = getToken();
				if (StringUtils.isNotBlank(result)) {
					CACHESERVICE.set(ACCESSTOKEN_CACHE_KEY, result, MAX_TIME, TimeUnit.SECONDS);
				}
			}
			if (log.isInfoEnabled()) {
				log.info("获取access_token返回数据 ={}", result);
			}
			return result;
		} finally {
			DISTRIBUTIONLOCK.unLock(ACCESSTOKEN_CACHE_KEY);
		}
	}
	/**
	 * 
	* @Title: refreshToken  
	* @Description:刷新token  
	* @param @return
	* @param @throws Exception    参数  
	* @return String    返回类型  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public static String refreshToken(String token) throws Exception {
		Assert.notNull(token, "refresh token is null");
		try {
			DISTRIBUTIONLOCK.lock(ACCESSTOKEN_CACHE_KEY);
			String result = (String) CACHESERVICE.get(ACCESSTOKEN_CACHE_KEY);
			if (StringUtils.isBlank(result) || token.equals(result)) {
				result = getToken();
				if (StringUtils.isNotBlank(result)) {
					CACHESERVICE.set(ACCESSTOKEN_CACHE_KEY, result, MAX_TIME, TimeUnit.SECONDS);
				}
			}
			if (log.isInfoEnabled()) {
				log.info("获取access_token返回数据 ={}", result);
			}
			return result;
		} finally {
			DISTRIBUTIONLOCK.unLock(ACCESSTOKEN_CACHE_KEY);
		}
	}
	/**
	 * 
	* @Title: getToken  
	* @Description: 获取token 
	* @param: @return
	* @param: @throws Exception
	* @return String
	* @author lijie
	* @throws
	 */
	private static String getToken() throws Exception {
		final Map<String, Object> params = new HashMap<>(3);
		params.put("grant_type", WeChatConfig.GRANT_TYPE);
		params.put("appid", WeChatConfig.APPID);
		params.put("secret", WeChatConfig.SECRET);
		String response = HttpClientUtils.sendGet(WeChatConfig.TOKEN_URL, params).getResponseContent();
		if (log.isInfoEnabled()) {
			log.info("获取access_token请求响应数据 ={}", response);
		}
		if (StringUtils.isNotBlank(response)) {
			TokenResponse ws = JSON.parseObject(response, TokenResponse.class);
			if (null != ws) {
				return ws.getAccess_token();
			}
		}
		return null;
	}
	/**
	 * 
	* @Title: getTicket  
	* @Description:获取微信  ticket
	* @param @param accessToken
	* @param @return
	* @param @throws Exception    参数  
	* @return String    返回类型  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public static String getTicket(final String accessToken) throws Exception {
		try {
			DISTRIBUTIONLOCK.lock(TICKET_CACHE_KEY);
			String result = (String) CACHESERVICE.get(TICKET_CACHE_KEY);
			if (StringUtils.isBlank(result)) {
				if (StringUtils.isNotBlank(accessToken)) {
					final Map<String, Object> params = new HashMap<>();
					params.put("type", WeChatConfig.TICKET_TYPE);
					String response = WeChatRquestUtil.sendGetByToken(WeChatConfig.TICKET_URL, params);
					if (StringUtils.isNotBlank(response)) {
						SignatureResponse sr = JSON.parseObject(response, SignatureResponse.class);
						if (null != sr) {
							result = sr.getTicket();
							CACHESERVICE.set(TICKET_CACHE_KEY, result, MAX_TIME, TimeUnit.SECONDS);
						}
					}
				}
			}
			log.info("获取ticket 返回数据={}", result);
			return result;
		} finally {
			DISTRIBUTIONLOCK.unLock(TICKET_CACHE_KEY);
		}
	}
	
	/**
	 * 
	 * @param decript
	 * @return
	 */
	public static String genSha(String decript) {
		String signature = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(decript.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signature;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
