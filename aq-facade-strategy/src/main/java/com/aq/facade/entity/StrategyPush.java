package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略推送记录表
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_push")
@Data
public class StrategyPush implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 策略id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

    /**
     * 股票代码
     */
    @Column(name = "sharesCode")
    private String sharesCode;

    /**
     * 股票名称
     */
    @Column(name = "sharesName")
    private String sharesName;

    /**
     * 交易时间
     */
    @Column(name = "tradeTime")
    private Date tradeTime;

    /**
     * 方向 0：卖 1：买
     */
    @Column(name = "direction")
    private Byte direction;

    /**
     * 成交数量
     */
    @Column(name = "tradeNumber")
    private Integer tradeNumber;

    /**
     * 成交单价
     */
    @Column(name = "tradePrice")
    private BigDecimal tradePrice;

    /**
     * 成交额
     */
    @Column(name = "tradeTotalPrice")
    private BigDecimal tradeTotalPrice;
}