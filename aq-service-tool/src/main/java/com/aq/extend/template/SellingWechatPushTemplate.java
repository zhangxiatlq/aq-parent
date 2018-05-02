package com.aq.extend.template;

import java.util.HashMap;
import java.util.Map;

import com.aq.core.constant.ColorEnum;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.core.wechat.constant.WechatTemplateEnum;
import com.aq.core.wechat.dto.WechatPushTemplateDTO;
import com.aq.core.wechat.template.WeChatPushTemplateEnum;
import com.aq.core.wechat.template.WechatPushTemplateGenerate;
import com.aq.core.wechat.template.data.TemplateData;
import com.aq.extend.template.data.ToolTemplateData;
import com.aq.facade.contant.ToolStatusEnum.Pushdirection;
/**
 * 
 * @ClassName: SellingWechatPushTemplate
 * @Description: 买卖点工具模板
 * @author: lijie
 * @date: 2018年4月17日 上午10:44:31
 */
public class SellingWechatPushTemplate extends WechatPushTemplateGenerate {

	@Override
	public WechatPushTemplateDTO createWechatPushTemplate(TemplateData data) {
		ToolTemplateData toolTem = (ToolTemplateData) data;
		// 执行工具微信模板推送
		WechatPushTemplateDTO pdto = new WechatPushTemplateDTO();
		pdto.setTouser(data.getOpenId());
		pdto.setUrl("");
		Map<String, com.aq.core.wechat.dto.WechatPushTemplateDTO.Data> dataMap = new HashMap<String, com.aq.core.wechat.dto.WechatPushTemplateDTO.Data>();
		dataMap.put("remark", initData(WeChatPushTemplateEnum.PROMPT.getMessage()));
		String name = toolTem.getUserName() == null ? "：" : toolTem.getUserName() + "：";
		dataMap.put("first", initData("欢迎使用" + ToolCategoryEnum.SELLING.getDesc() + "实时推送服务，" + name));
		pdto.setTemplate_id(WechatTemplateEnum.TWO.getTemplateId());
		if (Pushdirection.PURCHASE.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("买入", ColorEnum.RED.getHex()));
		} else if (Pushdirection.SELL_OUT.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("卖出", ColorEnum.GREEN.getHex()));
		}
		dataMap.put("keyword2", initData(toolTem.getStockCode()));
		dataMap.put("keyword3", initData(toolTem.getStockName()));
		dataMap.put("keyword4", initData(toolTem.getPrice()));
		dataMap.put("keyword5", initData("0"));
		pdto.setData(dataMap);
		return pdto;
	}

}
