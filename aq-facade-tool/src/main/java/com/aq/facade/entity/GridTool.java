package com.aq.facade.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
* @ClassName: GridTool 
* @Description: 网格工具表实体
* @author lijie
* @date 2018年1月19日 下午5:19:43 
*
 */
@Data
@Table(name="aq_grid_tool")
public class GridTool extends BaseTool {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 7270664543757515188L;
	/**
	 * 委托数量
	 */
	@Column(name="entrustNum")
	private Integer entrustNum;
	/**
	 * 基础价
	 */
	@Column(name="basePrice")
	private BigDecimal basePrice;
	/**
	 * 价差
	 */
	@Column(name="differencePrice")
	private BigDecimal differencePrice;
	/**
	 * 上限价
	 */
	@Column(name="upperPrice")
	private BigDecimal upperPrice;
	/**
	 * 下限价
	 */
	@Column(name="lowerPrice")
	private BigDecimal lowerPrice;

}
