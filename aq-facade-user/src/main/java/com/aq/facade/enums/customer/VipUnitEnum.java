package com.aq.facade.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: VipEnum
 * @Description: vip相关枚举
 * @author: lijie
 * @date: 2018年2月23日 下午4:11:00
 */
@AllArgsConstructor
@Getter
public enum VipUnitEnum {
	/**
	 * 年
	 */
	YEAR("year"),
	/**
	 * 月
	 */
	MONTH("month"),
	/**
	 * 天
	 */
	DAY("day");
	/**
	 * 单位
	 */
	private String unit;
}
