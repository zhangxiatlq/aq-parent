package com.aq.service;

import com.aq.facade.vo.order.OrderCacheInfoVO;
/**
 * 
 * @ClassName: IOrderCacheService
 * @Description: 获取缓存订单数据
 * @author: lijie
 * @date: 2018年3月14日 上午10:55:13
 */
public interface IOrderCacheService {
	/**
	 * 
	* @Title: getCacheOrder  
	* @Description: 获取订单数据 
	* @param: @param orderNo
	* @param: @return
	* @return OrderCacheInfoVO
	* @author lijie
	* @throws
	 */
	OrderCacheInfoVO getCacheOrder(String orderNo);
	/**
	 * 
	* @Title: putCacheOrder  
	* @Description: 添加订单缓存 
	* @param: @param info
	* @param: @return
	* @return OrderCacheInfoVO
	* @author lijie
	* @throws
	 */
	OrderCacheInfoVO putCacheOrder(OrderCacheInfoVO info);
	/**
	 * 
	* @Title: evictCacheOrder  
	* @Description: 清楚订单缓存 
	* @param: @param orderNo
	* @return void
	* @author lijie
	* @throws
	 */
	void evictCacheOrder(String orderNo);
}
