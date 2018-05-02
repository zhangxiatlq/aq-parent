package com.aq.facade.contant;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: OrderByTypeEnum
 * @Description: 排序类型
 * @author: lijie
 * @date: 2018年3月30日 上午11:42:12
 */
@Getter
@AllArgsConstructor
public enum OrderByTypeEnum {

	ASC("asc","正序"),
	DESC("desc","倒序");
	
	private String type;
	
	private String desc;
	
	/**
	 * 
	* @Title: type  
	* @Description: TODO 
	* @param: @param type
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	public static String type(String type) {
		if (StringUtils.isNotBlank(type)) {
			OrderByTypeEnum[] types = OrderByTypeEnum.values();
			for (OrderByTypeEnum t : types) {
				if (type.equalsIgnoreCase(t.getType())) {
					return t.getType();
				}
			}
		}
		return OrderByTypeEnum.DESC.type;
	}
}
