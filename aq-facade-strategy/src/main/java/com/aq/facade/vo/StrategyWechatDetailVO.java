package com.aq.facade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 策略微信详情VO
 *
 * @author 熊克文
 * @createDate 2018\2\13
 **/
@Data
public class StrategyWechatDetailVO implements Serializable {

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
     * 策略描述
     */
    @ApiModelProperty(value = "策略描述")
    private String strategyDesc;

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
}
