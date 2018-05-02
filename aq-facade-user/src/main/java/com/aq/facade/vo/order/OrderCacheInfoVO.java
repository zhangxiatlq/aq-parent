package com.aq.facade.vo.order;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: OrderCacheInfoVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月26日 下午1:31:12
 */
@Data
public class OrderCacheInfoVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -6630487124082086689L;
	/**
	 * 订单金额
	 */
	private String price;
	/**
	 * 订单编号
	 */
	private String orderNo;
}
