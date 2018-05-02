package com.aq.facade.dto.synchro;

import lombok.Data;
/**
 * 
 * @ClassName: SynPythonGridDTO
 * @Description: 网格工具传输实体
 * @author: lijie
 * @date: 2018年3月16日 上午10:09:27
 */
@Data
public class SynPythonGridDTO extends SynPythonBaseDTO {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4143642198395913676L;
	/**
	 * 委托数量
	 */
	private String entrustNum;
	/**
	 * 基础价
	 */
	private String basePrice;
	/**
	 * 价差
	 */
	private String differencePrice;
	/**
	 * 上限价
	 */
	private String upperPrice;
	/**
	 * 下限价
	 */
	private String lowerPrice;
}
