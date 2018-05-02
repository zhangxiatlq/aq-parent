package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾推送记录表vo
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "投顾推送记录信息")
public class AdviserPushQueryVO implements Serializable {
    private static final long serialVersionUID = 5756377067741835763L;

    @ApiModelProperty(value = "推送id")
    private Integer id;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tradeTime;

    /**
     * 方向 -1：卖 1：买
     * {@link com.aq.facade.contant.DirectionEnum}
     */
    @ApiModelProperty(value = "方向 -1：卖 1：买")
    private String direction;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交数量")
    private Integer tradeNumber;

    /**
     * 委托单价
     */
    @ApiModelProperty(value = "委托单价")
    private BigDecimal tradePrice;

    /**
     * 成交额
     */
    @ApiModelProperty(value = "成交额")
    private BigDecimal tradeTotalPrice;

    /**
     * 成交状态 1-已成 2-未成 3-已撤
     */
    @ApiModelProperty(value = "成交状态 1-已成 2-未成 3-已撤")
    private Byte tradeStatus;

    /**
     * 成交单价
     */
    @ApiModelProperty(value = "成交单价")
    private String realPrice;
}
