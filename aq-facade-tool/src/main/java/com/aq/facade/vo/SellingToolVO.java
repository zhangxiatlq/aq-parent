package com.aq.facade.vo;

import lombok.Data;
/**
 * 
 * @ClassName: SellingToolVO
 * @Description: 卖点工具信息
 * @author: lijie
 * @date: 2018年2月11日 下午3:07:29
 */
@Data
public class SellingToolVO extends BaseToolVO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 委托数量
	 */
	private Integer entrustNum;
	/**
	 * 短期均线价格
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
	 * 向下偏离值
	 */
	private String lowerDeviate;
}
