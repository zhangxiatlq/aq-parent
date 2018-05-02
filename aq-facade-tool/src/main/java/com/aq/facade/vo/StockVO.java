package com.aq.facade.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: StockVO
 * @Description: 股票相关数据
 * @author: lijie
 * @date: 2018年1月20日 下午6:46:10
 */
@Data
public class StockVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 股票代码
	 */
	private String stockCode;
	/**
	 * 股票名称
	 */
	private String stockName;
}
