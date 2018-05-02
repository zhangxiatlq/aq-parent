package com.aq.facade.enums;

import org.springframework.util.Assert;

import lombok.Getter;
/**
 * 
 * @ClassName: PrizeEnum
 * @Description: 奖项编码
 * @author: lijie
 * @date: 2018年1月25日 下午4:09:39
 */
public enum PrizeTypeEnum {
	
	VIP_DAY("P1000001","赠送vip天数"),
	BUY_GIVE_DAY("P1000002","买工具赠送vip天数")
	;
	
	@Getter
	private String code;
	@Getter
	private String desc;
	
	PrizeTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static PrizeTypeEnum getPrizeType(final String code) {
		PrizeTypeEnum result = null;
		if (null != code) {
			PrizeTypeEnum[] vs = PrizeTypeEnum.values();
			for (PrizeTypeEnum v : vs) {
				if (code.equals(v.getCode())) {
					result = v;
					break;
				}
			}
		}
		Assert.notNull(result, "prize is not exists");
		return result;
	}
}
