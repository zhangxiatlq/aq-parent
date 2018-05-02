package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdviserBaseVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 总资产
	 */
	@ApiModelProperty(value = "总资产")
	private String totalAssets;
	/**
	 * 可用资产
	 */
	@ApiModelProperty(value = "可用资产")
	private String availableAssets;
	/**
	 * 参考市值
	 */
	@ApiModelProperty(value = "参考市值")
	private String marketValue;
	/**
	 * 参考盈亏
	 */
	@ApiModelProperty(value = "参考盈亏")
	private String referenceProfit;
	/**
	 * 今日收益
	 */
	@ApiModelProperty(value = "今日收益")
	private String todayRate;
	/**
	 * 冻结资金
	 */
	@ApiModelProperty(value = "冻结资金")
	private String totalFreezeNum;
}
