package com.aq.extend.template;

import org.springframework.stereotype.Component;

import com.aq.core.constant.ToolCategoryEnum;
import com.aq.core.wechat.template.WechatPushTemplateFactory;
import com.aq.core.wechat.template.WechatPushTemplateGenerate;
/**
 * 
 * @ClassName: WechatPushTemplate
 * @Description: 模板生成工厂
 * @author: lijie
 * @date: 2018年4月17日 上午10:35:35
 */
@Component
public class ToolWechatPushTemplate implements WechatPushTemplateFactory {

	@Override
	public WechatPushTemplateGenerate createTemplate(ToolCategoryEnum type) {
		switch (type) {
		case GRID:
			return new GridWechatPushTemplate();
		case SELLING:
			return new SellingWechatPushTemplate();
		case TREND:
			return new TrendWechatPushTemplate();
		default:
			break;
		}
		throw new RuntimeException("create wechat push template type is null");
	}

}
