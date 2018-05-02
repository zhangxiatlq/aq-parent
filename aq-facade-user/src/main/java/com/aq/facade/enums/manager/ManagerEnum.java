package com.aq.facade.enums.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: ManagerEnum
 * @Description: 客户经理枚举
 * @author: lijie
 * @date: 2018年2月11日 上午11:57:56
 */
@AllArgsConstructor
@Getter
public enum ManagerEnum {

	AUTHEN((byte)1,"已实名认证"),
	NOT_AUTHEN((byte)2,"未实名认证"),
	
	BINDBANK((byte)1,"已绑定银行卡"),
	NOT_BINDBANK((byte)2,"未绑定银行卡")
	
	;
	
	private Byte status;
	
    private String desc;
}
