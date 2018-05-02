package com.aq.core.constant;

import lombok.Getter;
/**
 * 
* @ClassName: ConfigEnum 
* @Description: 订阅频道配置信息 
* @author lijie
* @date 2017年11月28日 下午5:30:24 
*
 */
public enum ChannelEnum {

	STOCK_CHANNEL("STOCK:TOPIC");
	
	@Getter
	private String channelName;
	
	ChannelEnum (String channelName){
		this.channelName = channelName;
	}
}
