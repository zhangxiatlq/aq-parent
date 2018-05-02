package com.aq.facade.contant;

import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: ToolTypeEnum
 * @Description: 工具类型
 * @author: lijie
 * @date: 2018年4月16日 下午3:18:41
 */
@Getter
@AllArgsConstructor
public enum ToolTypeEnum {

	ROUTINE((byte)1,"常规工具"),
	
	SPECIAL_PURPOSE((byte)2,"专用工具");
	
	private Byte type;
	
	private String desc;
	
	public static Byte type(final Byte type) {
		Byte result = null;
		if (null != type) {
			ToolTypeEnum[] typs = ToolTypeEnum.values();
			for (ToolTypeEnum ty : typs) {
				if (type.equals(ty.getType())) {
					result = ty.getType();
					break;
				}
			}
		}
		Assert.notNull(result, "tool type is not exists");
		return result;
	}

	public static ToolTypeEnum typeEnum(final Byte type) {
		ToolTypeEnum result = null;
		if (null != type) {
			ToolTypeEnum[] typs = ToolTypeEnum.values();
			for (ToolTypeEnum ty : typs) {
				if (type.equals(ty.getType())) {
					result = ty;
					break;
				}
			}
		}
		Assert.notNull(result, "tool type enum is not exists");
		return result;
	}
}
