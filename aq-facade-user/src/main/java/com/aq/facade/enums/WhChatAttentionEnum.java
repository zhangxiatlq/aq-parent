package com.aq.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: WhChatAttentionEnum
 * @Description: 是否取消关注
 * @author: lijie
 * @date: 2018年3月15日 下午8:45:00
 */
@AllArgsConstructor
@Getter
public enum WhChatAttentionEnum {

	CANCEL_ATTENTION((byte)1,"取消关注"),
	ATTENTION((byte)2,"未取消关注");
	/**
	 * 
	 */
	private Byte status;
	
	private String desc;
}
