package com.aq.facade.dto.synchro;

import lombok.Data;
/**
 * 
 * @ClassName: SynPythonSellingDTO
 * @Description: 买卖点
 * @author: lijie
 * @date: 2018年3月16日 上午10:12:45
 */
@Data
public class SynPythonSellingDTO extends SynPythonBaseDTO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -7067576751347256161L;
	/**
	 * 委托数量
	 */
	private String entrustNum;
	/**
	 * 短期均线天数
	 */
	private String shortDay;
	/**
	 * 长期均线价格
	 */
	private String longDay;
	/**
	 * 向上偏离值
	 */
	private String topDeviate;
	/**
	 * 向下偏离
	 */
	private String lowerDeviate;
}
