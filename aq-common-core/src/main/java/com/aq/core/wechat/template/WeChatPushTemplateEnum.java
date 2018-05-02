package com.aq.core.wechat.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: WeChatPushTemplate
 * @Description: 微信推送模板提示枚举
 * @author: lijie
 * @date: 2018年4月19日 上午9:20:47
 */
@Getter
@AllArgsConstructor
public enum WeChatPushTemplateEnum {

	PROMPT("AQ量化家说明：市场有风险，投资需谨慎。本平台只作展示与您对应作者的模拟交易记录，并不提供任何影响您未来证券操作方向或判断的建议。【AQ量化家】");
	
	private String message;
}
