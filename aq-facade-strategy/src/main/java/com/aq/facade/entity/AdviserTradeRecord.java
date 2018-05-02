package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾指标记录表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_trade_record")
@Data
public class AdviserTradeRecord implements Serializable {
    private static final long serialVersionUID = 74422799227492212L;

    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 投顾id
     */
    @Column(name = "adviserId")
    private Integer adviserId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 指标时间
     */
    @Column(name = "indexTime")
    private Date indexTime;

    /**
     * 创建人
     */
    @Column(name = "createId")
    private Integer createId;

    /**
     * 累计收益(策略从开始那天累积的收益率，我的收益，百分比数值)
     */
    @Column(name = "cumulativeIncome")
    private BigDecimal cumulativeIncome;

    /**
     * 年化收益
     */
    @Column(name = "annualIncome")
    private BigDecimal annualIncome;

    /**
     * 最大回撤（每天的收益情况和这天之前的最大值的一个比值，称为回撤，所有回撤里最大的一个值即最大回撤）
     */
    @Column(name = "maxRetracement")
    private BigDecimal maxRetracement;

    /**
     * 收益回撤比（最大回撤和年化收益的比值）
     */
    @Column(name = "returnRetracementRatio")
    private BigDecimal returnRetracementRatio;

    /**
     * 投顾超过同期基准
     */
    @Column(name = "adviserPeriod")
    private BigDecimal adviserPeriod;

    /**
     * 夏普率（单位风险所产生的收益）
     */
    @Column(name = "sharpRate")
    private BigDecimal sharpRate;

    /**
     * 总资产（策略目前的所有资产）
     */
    @Column(name = "totalAssets")
    private BigDecimal totalAssets;

    /**
     * 可用资产（改为可用资金，即手上的现金）
     */
    @Column(name = "availableAssets")
    private BigDecimal availableAssets;

    /**
     * 参考市值（手上股票的按当日收盘价计算的总市值）
     */
    @Column(name = "marketValue")
    private BigDecimal marketValue;

    /**
     * 今日收益率
     */
    @Column(name = "todayRate")
    private BigDecimal todayRate;

    /**
     * 基准指数值（基准指数的数值，百分比数值）
     */
    @Column(name = "numericValue")
    private BigDecimal numericValue;

    /**
     * 波动率
     */
    @Column(name = "volatility")
    private BigDecimal volatility;

    /**
     * 今日回测
     */
    @Column(name = "todayRetracement")
    private BigDecimal todayRetracement;

}