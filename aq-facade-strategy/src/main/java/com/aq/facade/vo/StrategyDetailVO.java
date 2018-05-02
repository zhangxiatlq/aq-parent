package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 策略详情VO
 *
 * @author 熊克文
 * @createDate 2018\2\9 0009
 **/
@Data
@ApiModel(value = "策略详情VO")
public class StrategyDetailVO implements Serializable {

    /**
     * 策略id
     */
    @ApiModelProperty(value = "策略id")
    private Integer id;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    /**
     * 持仓数量
     */
    @ApiModelProperty(value = "持仓数量")
    private Integer positionTotalCount;

    /**
     * 交易数量
     */
    @ApiModelProperty(value = "交易数量")
    private Integer pushTotalCount;

    /**
     * 推荐数量
     */
    @ApiModelProperty(value = "推荐数量")
    private Integer recommendTotalCount;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer purchaseTotalCount;

    /**
     * 策略指标记录list
     */
    @ApiModelProperty(value = "策略指标记录list")
    private List<StrategyTradeRecordQueryVO> strategyTradeRecordQueryVOList;

    /**
     * 策略基准指数list
     */
    @ApiModelProperty(value = "策略基准指数list")
    private List<StrategyBenchmarkQueryVO> strategyBenchmarkQueryVOList;

    /**
     * 策略持仓list
     */
    @ApiModelProperty(value = "策略持仓list")
    private List<StrategyPositionVO> strategyPositionVOList;

    /**
     * 策略推送list
     */
    @ApiModelProperty(value = "策略推送list")
    private List<StrategyPushQueryVO> strategyPushQueryVOList;

    /**
     * 策略推荐list
     */
    @ApiModelProperty(value = "策略推荐list")
    private List<StrategyRecommendVO> strategyRecommendVOList;

    /**
     * 策略购买记录
     */
    @ApiModelProperty(value = "策略购买记录")
    private List<StrategyPurchaseVO> strategyPurchaseVOList;
}
