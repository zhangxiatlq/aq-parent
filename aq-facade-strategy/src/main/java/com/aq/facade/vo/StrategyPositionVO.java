package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 策略持仓VO
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Data
@ApiModel(value = "策略持仓")
public class StrategyPositionVO implements Serializable {


    /**
     * 股票代码
     */
    @ApiModelProperty(value = "股票代码")
    private String sharesCode;

    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称")
    private String sharesName;

    /**
     * 数量(持仓数量)
     */
    @ApiModelProperty(value = "持仓数量")
    private Integer holdingNo;

    /**
     * 参考成本
     */
    @ApiModelProperty(value = "参考成本")
    private BigDecimal referenceCost;

    /**
     * 参考盈利
     */
    @ApiModelProperty(value = "参考盈利")
    private BigDecimal referenceProfit;

}