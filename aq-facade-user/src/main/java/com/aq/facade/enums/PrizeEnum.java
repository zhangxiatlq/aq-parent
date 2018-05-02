package com.aq.facade.enums;

import lombok.Getter;

/**
 * 
 * @ClassName: PrizeEnum
 * @Description: 奖项枚举
 * @author: lijie
 * @date: 2018年1月27日 上午11:30:25
 */
public enum PrizeEnum {

	RECEIVE((byte)1,"已领取"),
	NOT_RECEIVE((byte)0,"未领取"),
	VALID((byte)1,"有效"),
	NOT_VALID((byte)0,"无效 ")
	;
	@Getter
	private Byte status;
	
	@Getter
	private String desc;
	
	private PrizeEnum(Byte status, String desc) {
		this.status = status;
		this.desc = desc;
	}
	
}
