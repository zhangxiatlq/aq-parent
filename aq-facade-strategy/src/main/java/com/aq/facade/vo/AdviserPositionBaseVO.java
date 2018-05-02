package com.aq.facade.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdviserPositionBaseVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 数量(持仓数量)
     */
    @ApiModelProperty(value = "持仓数量")
    private Integer holdingNo;

    /**
     * 参考成本
     */
    @ApiModelProperty(value = "参考成本")
    private String referenceCost;

    /**
     * 参考盈利
     */
    @ApiModelProperty(value = "参考盈亏")
    private String referenceProfit;
    /**
     * 买入冻结
     */
    @ApiModelProperty(value = "买入冻结")
    private Integer freezeNum;
    /**
     * 盈利比例
     */
    @ApiModelProperty(value = "盈利比例")
    private String profitRatio;
    
}
