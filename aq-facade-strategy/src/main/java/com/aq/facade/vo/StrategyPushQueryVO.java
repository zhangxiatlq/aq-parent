package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略推送信息VO
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Data
@ApiModel(value = "策略推送信息")
public class StrategyPushQueryVO implements Serializable {

    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称")
    private String sharesName;
    /**
     * 股票代码
     */
    @ApiModelProperty(value = "股票代码")
    private String sharesCode;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date tradeTime;

    /**
     * 方向 0：卖 1：买
     * {@link com.aq.core.constant.SharesDirectionEnum}
     */
    @ApiModelProperty(value = "方向 0：卖 1：买")
    private String direction;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交数量")
    private Integer tradeNumber;

    /**
     * 成交单价
     */
    @ApiModelProperty(value = "成交单价")
    private BigDecimal tradePrice;

    /**
     * 成交额
     */
    @ApiModelProperty(value = "成交额")
    private BigDecimal tradeTotalPrice;
}