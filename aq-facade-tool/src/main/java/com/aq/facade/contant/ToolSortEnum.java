package com.aq.facade.contant;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: ToolSortEnum
 * @Description: 工具排序
 * @author: lijie
 * @date: 2018年3月30日 上午11:11:07
 */
@Getter
@AllArgsConstructor
public enum ToolSortEnum {
	
	CREATETIME("createTime","添加时间"),
	
	REALNAME("realName","客户真实姓名"),
	
	STOCKCODE("stockCode","股票代码"),
	
	STOCKNAME("stockName","股票名称"),
	
	BASEPRICE("basePrice","基础价格"),
	
	DIFFERENCEPRICE("differencePrice","价差"),
	
	ENTRUSTNUM("entrustNum","委托数"),
	
	UPPERPRICE("upperPrice","价格上限"),
	
	LOWERPRICE("lowerPrice","价格下限"),
	
	RECOMMEND("recommend","推荐");
	
	private String field;
	
	private String desc;
	/**
	 * 
	* @Title: field  
	* @Description: 得到排序字段 
	* @param: @param field
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	public static String field(String field) {
		if (StringUtils.isNotBlank(field)) {
			ToolSortEnum[] ts = ToolSortEnum.values();
			for (ToolSortEnum t : ts) {
				if (field.equals(t.getField())) {
					return t.getField();
				}
			}
		}
		return ToolSortEnum.CREATETIME.getField();
	}
}
