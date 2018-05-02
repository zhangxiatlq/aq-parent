package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略持仓表（列表只显示一支股票最新的持仓记录）
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_position")
@Data
public class StrategyPosition implements Serializable {

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
     * 数量(持仓数量)
     */
    @Column(name = "holdingNo")
    private Integer holdingNo;

    /**
     * 参考成本
     */
    @Column(name = "referenceCost")
    private BigDecimal referenceCost;

    /**
     * 参考盈利
     */
    @Column(name = "referenceProfit")
    private BigDecimal referenceProfit;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

}