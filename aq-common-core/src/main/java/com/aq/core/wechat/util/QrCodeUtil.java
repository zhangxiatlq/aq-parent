package com.aq.core.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.core.wechat.response.QrCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @ClassName: QrCodeUtil
 * @Description: 二维码生成地址
 * @author: lijie
 * @date: 2018年3月13日 下午5:46:10
 */
@Slf4j
public class QrCodeUtil {

	/**
	 * 
	* @Title: getQrCodeResponse  
	* @Description: 二维码生成获取 ticket
	* @param: @return
	* @param: @throws Exception
	* @return QrCodeResponse
	* @author lijie
	* @throws
	 */
	public static QrCodeResponse getQrCodeTicket(final String telphone, final Byte role) {
		QrCodeResponse result = null;
		try {
			JSONObject json = new JSONObject();
			JSONObject info = new JSONObject();
			JSONObject scene = new JSONObject();
			json.put("action_name", "QR_LIMIT_STR_SCENE");
			scene.put("scene_str", role + WheChatConstant.BIND_IDEN + telphone);
			info.put("scene", scene);
			json.put("action_info", info);
			String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.QR_CODE, json.toJSONString());
			log.info("二维码生成获取ticket请求响应={}", response);
			if (StringUtils.isNotBlank(response)) {
				result = JSONObject.parseObject(response, QrCodeResponse.class);
			}
		} catch (Exception e) {
			log.error("二维码生成获取 ticket 异常", e);
		}
		return result;
	}
	
}
