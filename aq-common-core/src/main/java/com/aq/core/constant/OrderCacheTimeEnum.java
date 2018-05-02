package com.aq.core.constant;

import java.util.concurrent.TimeUnit;

/**
 * 
* @ClassName: OrderCacheTimeEnum 
* @Description: 订单缓存时间 
* @author lijie
* @date 2017年9月19日 下午12:00:45 
*
 */
public enum OrderCacheTimeEnum {

	ORDER_OVERTIME(2*60L,"订单缓存时间",TimeUnit.MINUTES);
	/**
	 * 超时时间
	 */
	private Long time;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 单位
	 */
	private TimeUnit timeUnit;
	
	private OrderCacheTimeEnum(Long time,String desc,TimeUnit timeUnit){
		this.time = time;
		this.desc = desc;
		this.timeUnit = timeUnit;
	}

	public Long getTime() {
		return time;
	}

	public String getDesc() {
		return desc;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
}
