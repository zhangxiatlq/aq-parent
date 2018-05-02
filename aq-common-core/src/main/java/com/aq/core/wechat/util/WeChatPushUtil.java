package com.aq.core.wechat.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.core.wechat.dto.WechatPushTemplateDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: WhChatPush
 * @Description: 微信推送
 * @author: lijie
 * @date: 2018年2月22日 下午2:42:36
 */
@Slf4j
public class WeChatPushUtil {
	
	/**
	 * @throws Exception 
	* @Title: pushWechat  
	* @Description: 微信模板推送 
	* @param @param accessToken
	* @param @param dto
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public static boolean pushWechat(WechatPushTemplateDTO dto) {
		return handlerPush(null, dto);
	}
	/**
	 * 
	* @Title: pushWechat  
	* @Description:  微信模板推送 传递accessToken
	* @param @param accessToken
	* @param @param dto
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public static boolean pushWechat(String accessToken, WechatPushTemplateDTO dto) {

		return handlerPush(accessToken, dto);
	}
	/**
	 * 
	* @Title: handlerPush  
	* @Description: 推送实际处理 
	* @param @param accessToken
	* @param @param dto
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	private static boolean handlerPush(String accessToken, WechatPushTemplateDTO dto) {
		log.info("推送消息参数,dto:{}", JSON.toJSONString(dto));
		try {
			if (StringUtils.isBlank(accessToken)) {
				accessToken = WeChatSignatureUtil.getAccessToken();
			}
			String msg = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.PUSH_URL, JSON.toJSONString(dto),
					accessToken);
			JSONObject result = JSONObject.parseObject(msg);
			log.info("微信推送返回数据={}", result.toJSONString());
			return WheChatConstant.SUCCESS_STATE.equals(result.getString("errcode"));
		} catch (Exception e) {
			log.error("调用微信模板推送异常", e);
		}
		return false;
	}
}
