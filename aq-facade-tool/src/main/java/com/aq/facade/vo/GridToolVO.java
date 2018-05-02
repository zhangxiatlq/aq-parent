package com.aq.facade.vo;

import lombok.Data;
/**
 * 
 * @ClassName: GridToolVO
 * @Description: 网格工具信息数据
 * @author: lijie
 * @date: 2018年2月11日 下午3:08:29
 */
@Data
public class GridToolVO extends BaseToolVO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -3365078699157438389L;
	/**
	 * 委托数量
	 */
	private Integer entrustNum;
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
