package com.aq.facade.dto;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SynToolDTO
 * @Description: 同步工具传输实体
 * @author: lijie
 * @date: 2018年2月12日 下午7:34:45
 */
@Data
public class SynToolDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8748425431462865199L;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 股票代码
	 */
	private String stockCode;
	/**
	 * 委托数量
	 */
	private Integer entrustNum;

	// ----------网格-----------
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

	// -------------卖点--------
	/**
	 * 短期均线天数
	 */
	private Integer shortDay;
	/**
	 * 长期均线天数
	 */
	private Integer longDay;
	/**
	 * 向上偏离值
	 */
	private String topDeviate;
	/**
	 * 向下偏离
	 */
	private String lowerDeviate;
}
