package com.aq.facade.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: VipOrderStatusEnum
 * @Description: vip订单状态
 * @author: lijie
 * @date: 2018年2月23日 下午4:31:29
 */
@AllArgsConstructor
public enum VipOrderStatusEnum {

	NO_DELETE((byte) 0, "未删除"),
	YES_DELETE((byte) 1, "已删除");
	
	@Getter
	private Byte status;

	@Getter
	private String desc;
}
