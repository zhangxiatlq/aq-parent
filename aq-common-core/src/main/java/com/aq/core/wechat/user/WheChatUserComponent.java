package com.aq.core.wechat.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.core.wechat.response.UserInfoResponse;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.util.http.HttpClientUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: WheChatUser
 * @Description: 微信用户信息
 * @author: lijie
 * @date: 2018年3月13日 上午9:45:14
 */
@Slf4j
@Component
public class WheChatUserComponent {
	/**
	 * 
	* @Title: getUserInfo  
	* @Description: 获取微信用户信息 
	* @param: @param openid
	* @param: @return
	* @return UserInfoResponse
	* @author lijie
	* @throws
	 */
	public UserInfoResponse getUserInfo(final String openid) {
		UserInfoResponse result = null;
		Map<String, Object> params = new HashMap<>(3);
		try {
			String accessToken = WeChatSignatureUtil.getAccessToken();
			params.put("access_token", accessToken);
			params.put("openid", openid);
			params.put("lang", "zh_CN");
			String response = HttpClientUtils.sendGet(WeChatConfig.GET_UEER_INGFO, params).getResponseContent();
			JSONObject json = JSONObject.parseObject(response);
			log.info("获取微信用户信息异常返回数据={}", json.toJSONString());
			String errorCode = json.getString("errcode");
			// 失效则刷新token
			if (WheChatConstant.ACCESSTOKEN_INVALID.equals(errorCode)) {
				params.put("access_token", WeChatSignatureUtil.refreshToken(accessToken));
				response = HttpClientUtils.sendGet(WeChatConfig.GET_UEER_INGFO, params).getResponseContent();
			}
			if (StringUtils.isNotBlank(response)) {
				result = JSON.parseObject(response, UserInfoResponse.class);
			}
		} catch (Exception e) {
			log.error("获取微信用户信息异常", e);
		}
		return result;
	}
	
}
