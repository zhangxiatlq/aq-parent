package com.aq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import com.aq.core.annotation.DefSubscribe;
import com.aq.core.constant.ChannelEnum;
import com.aq.core.rediscache.listener.ISubscribeListener;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: ToolSubscribeImpl
 * @Description: 工具订阅服务
 * @author: lijie
 * @date: 2018年2月27日 下午3:33:46
 */
@Slf4j
@Service
@DefSubscribe(channel = ChannelEnum.STOCK_CHANNEL, listener = ToolSubscribeImpl.class)
public class ToolSubscribeImpl implements ISubscribeListener {
	/**
	 * 工具缓存服务
	 */
	@Autowired
	private ToolCacheServiceImpl toolCacheService;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String state = new String(message.getBody());
		log.info("订阅操作缓存股票历史数据start={}", state);
		if ("do.dog".equals(state)) {
			toolCacheService.operHistorySharesData();
		}
	}

}
