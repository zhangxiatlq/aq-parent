package com.aq.core.wechat.template;

import com.aq.core.wechat.dto.WechatPushTemplateDTO;
import com.aq.core.wechat.template.data.TemplateData;
/**
 * 
 * @ClassName: WechatPushTemplate
 * @Description: 微信推送模板生成
 * @author: lijie
 * @date: 2018年4月17日 上午10:42:58
 */
public abstract class WechatPushTemplateGenerate {
	/**
	 * 
	* @Title: createWechatPushTemplate  
	* @Description: TODO 
	* @param: @param data
	* @param: @return
	* @return WechatPushTemplateDTO
	* @author lijie
	* @throws
	 */
	public abstract WechatPushTemplateDTO createWechatPushTemplate(TemplateData data);
	/**
	 * 
	* @Title: initData  
	* @Description: TODO 
	* @param: @param value
	* @param: @return
	* @return com.aq.core.wechat.dto.WechatPushTemplateDTO.Data
	* @author lijie
	* @throws
	 */
	protected com.aq.core.wechat.dto.WechatPushTemplateDTO.Data initData(String value){
		com.aq.core.wechat.dto.WechatPushTemplateDTO.Data data = com.aq.core.wechat.dto.WechatPushTemplateDTO.Data.builder();
		data.setValue(value);
		return data;
	}
	/**
	 * 
	* @Title: initData  
	* @Description: TODO 
	* @param: @param value
	* @param: @param color
	* @param: @return
	* @return com.aq.core.wechat.dto.WechatPushTemplateDTO.Data
	* @author lijie
	* @throws
	 */
	protected com.aq.core.wechat.dto.WechatPushTemplateDTO.Data initData(String value, String color) {
		com.aq.core.wechat.dto.WechatPushTemplateDTO.Data data = com.aq.core.wechat.dto.WechatPushTemplateDTO.Data
				.builder();
		data.setValue(value);
		data.setColor(color);
		return data;
	}
}
