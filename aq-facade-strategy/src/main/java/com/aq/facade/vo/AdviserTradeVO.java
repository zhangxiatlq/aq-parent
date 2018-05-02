package com.aq.facade.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: AdviserTradeVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年3月13日 下午2:12:09
 */
@Data
public class AdviserTradeVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2097884567825301211L;
	/**
	 * 总资产
	 */
	private String totalAssets;
	/**
	 * 可用资产
	 */
	private String availableAssets;
	/**
	 * 参考市值
	 */
	private String marketValue;
	/**
	 * 参考盈亏
	 */
	private String referenceProfit;
}
