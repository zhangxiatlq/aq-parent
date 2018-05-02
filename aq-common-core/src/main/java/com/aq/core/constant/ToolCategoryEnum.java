package com.aq.core.constant;

import org.springframework.util.Assert;

import lombok.Getter;

/**
 * 
 * @ClassName: ToolCategoryEnum
 * @Description: 工具类别枚举
 * @author: lijie
 * @date: 2018年1月20日 下午3:05:04
 */
public enum ToolCategoryEnum {

	GRID("TOOL100001","网格工具"),
	SELLING("TOOL100002","卖点工具"),
	TREND("TOOL100003","趋势量化工具");
	
	@Getter
	private String code;
	
	@Getter
	private String desc;
	
	ToolCategoryEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static ToolCategoryEnum getToolCategory(final String code) {
		ToolCategoryEnum result = null;
		if (null != code) {
			ToolCategoryEnum[] vs = ToolCategoryEnum.values();
			for (ToolCategoryEnum v : vs) {
				if (code.equals(v.getCode())) {
					result = v;
					break;
				}
			}
		}
		Assert.notNull(result,"tool is not exists");
		return result;
	}
}
