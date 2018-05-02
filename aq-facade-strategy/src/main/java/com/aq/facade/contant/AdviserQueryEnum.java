package com.aq.facade.contant;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: AdviserQueryEnum
 * @Description: 投顾查询信息
 * @author: lijie
 * @date: 2018年3月13日 下午2:05:35
 */
@Getter
@AllArgsConstructor
public enum AdviserQueryEnum {

	QUERY_TRADE((byte)1,"查询资产"),
	
	NO_QUERY_TRADE((byte)0,"不需要查询资产")
	;
	private Byte state;
	private String desc;
}
