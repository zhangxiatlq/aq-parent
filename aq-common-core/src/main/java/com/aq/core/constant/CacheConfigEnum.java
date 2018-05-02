package com.aq.core.constant;

import java.util.concurrent.TimeUnit;

import lombok.Getter;

/**
 * 
 * @ClassName: CacheConfigEnum
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月9日 上午11:35:02
 */
public enum CacheConfigEnum {

	/**
	 * 短信最大缓存时间
	 */
	SMS_MAX_TIME(5L,TimeUnit.MINUTES),
	
	USER_CACHE_TIME(480L,TimeUnit.MINUTES),
	/**
	 * 订单详情缓存时间
	 */
	ORDER_INFOCAHETIME(30L,TimeUnit.MINUTES);
	;
	
	/**
	 * 缓存时长
	 */
	@Getter
	private long duration;
	/**
	 * 时间单位
	 */
	@Getter
	private TimeUnit unit;
	
	private CacheConfigEnum(long duration, TimeUnit unit){
		this.duration = duration;
		this.unit = unit;
	}
}
