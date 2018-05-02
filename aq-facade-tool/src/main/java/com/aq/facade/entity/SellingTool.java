package com.aq.facade.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: SellingTool
 * @Description: 卖点工具
 * @author: lijie
 * @date: 2018年2月10日 下午3:03:42
 */
@Data
@Table(name="aq_selling_tool")
public class SellingTool extends BaseTool {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	/**
	 * 委托数量
	 */
	@Column(name="entrustNum")
	private Integer entrustNum;
	/**
	 * 短期均线天数
	 */
	@Column(name="shortDay")
	private Integer shortDay;
	/**
	 * 长期均线天数
	 */
	@Column(name="longDay")
	private Integer longDay;
	/**
	 * 向上偏离值
	 */
	@Column(name="topDeviate")
	private Double topDeviate;
	/**
	 * 向下偏离值
	 */
	@Column(name="lowerDeviate")
	private Double lowerDeviate;
}
