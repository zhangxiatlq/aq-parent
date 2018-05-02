package com.aq.core.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: TagEnum
 * @Description: 标签枚举
 * @author: lijie
 * @date: 2018年3月15日 下午1:43:59
 */
@Getter
@AllArgsConstructor
public enum TagEnum {

	FOLLOW_CUSTOMER_TAG(100,"客户关注标签ID"),

	FOLLOW_MANAGER_TAG(103,"客户经理关注标签ID");
	
	private int tagId;
	
	private String desc;
}
