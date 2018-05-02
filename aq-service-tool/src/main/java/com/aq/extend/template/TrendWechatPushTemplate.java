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
import com.aq.facade.contant.ToolTypeEnum;
/**
 * 
 * @ClassName: TrendWechatPushTemplate
 * @Description: 趋势量化
 * @author: lijie
 * @date: 2018年4月17日 上午10:49:29
 */
public class TrendWechatPushTemplate extends WechatPushTemplateGenerate {

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
		dataMap.put("first", initData("欢迎使用" + ToolCategoryEnum.TREND.getDesc()
				+ ((toolTem.getToolType() == ToolTypeEnum.SPECIAL_PURPOSE) ? "【专用】" : "") + "实时推送服务，" + name));
		pdto.setTemplate_id(WechatTemplateEnum.TWO.getTemplateId());
		if (Pushdirection.PURCHASE.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("看多", ColorEnum.RED.getHex()));
		} else if (Pushdirection.SELL_OUT.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("看空", ColorEnum.GREEN.getHex()));
		} else if (Pushdirection.CONTINUED_PURCHASE.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("持续看多", ColorEnum.RED.getHex()));
		} else if (Pushdirection.LOOK_ON.getDirection().equals(toolTem.getDirection())) {
			dataMap.put("keyword1", initData("观望"));
		}
		dataMap.put("keyword2", initData(toolTem.getStockCode()));
		dataMap.put("keyword3", initData(toolTem.getStockName()));
		dataMap.put("keyword4", initData(toolTem.getPrice()));
		dataMap.put("keyword5", initData("0"));
		pdto.setData(dataMap);
		return pdto;
	}

}
