package com.aq.core.wechat.template;

import com.aq.core.constant.ToolCategoryEnum;

/**
 * 
 * @ClassName: WechatPushTemplateFactory
 * @Description: 模板工厂
 * @author: lijie
 * @date: 2018年4月17日 上午10:32:12
 */
public interface WechatPushTemplateFactory {
	/**
	 * 
	* @Title: createTemplate  
	* @Description: 生成模板 
	* @param: @param data
	* @param: @return
	* @return WechatPushTemplateDTO
	* @author lijie
	* @throws
	 */
	public WechatPushTemplateGenerate createTemplate(ToolCategoryEnum type);
}
