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
 * @describe：投顾指标记录表vo
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "投顾指标记录")
public class AdviserTradeRecordQueryVO implements Serializable{

    private static final long serialVersionUID = -6551166308661799428L;

    /**
     * 累计收益(策略从开始那天累积的收益率，我的收益，百分比数值)
     */
    @ApiModelProperty(value = "累计收益")
    private BigDecimal cumulativeIncome;

    /**
     * 指标时间
     */
    @ApiModelProperty(value = "指标时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date indexTime;

    /**
     * 年化收益
     */
    @ApiModelProperty(value = "年化收益")
    private BigDecimal annualIncome;

    /**
     * 最大回撤（每天的收益情况和这天之前的最大值的一个比值，称为回撤，所有回撤里最大的一个值即最大回撤）
     */
    @ApiModelProperty(value = "最大回撤")
    private BigDecimal maxRetracement;

    /**
     * 收益回撤比（最大回撤和年化收益的比值）
     */
    @ApiModelProperty(value = "收益回撤比")
    private BigDecimal returnRetracementRatio;

    /**
     * 投顾超过同期基准
     */
    @ApiModelProperty(value = "投顾超过同期基准")
    private BigDecimal adviserPeriod;

    /**
     * 夏普率（单位风险所产生的收益）
     */
    @ApiModelProperty(value = "夏普率")
    private BigDecimal sharpRate;

    /**
     * 总资产（策略目前的所有资产）
     */
    @ApiModelProperty(value = "总资产")
    private BigDecimal totalAssets;

    /**
     * 可用资产（改为可用资金，即手上的现金）
     */
    @ApiModelProperty(value = "可用资产")
    private BigDecimal availableAssets;

    /**
     * 参考市值（手上股票的按当日收盘价计算的总市值）
     */
    @ApiModelProperty(value = "参考市值")
    private BigDecimal marketValue;

    /**
     * 今日收益率
     */
    @ApiModelProperty(value = "今日收益率")
    private BigDecimal todayRate;

    /**
     * 基准指数值（基准指数的数值，百分比数值）
     */
    @ApiModelProperty(value = "基准指数值")
    private BigDecimal numericValue;

    /**
     * 波动率
     */
    @ApiModelProperty(value = "波动率")
    private BigDecimal volatility;

    /**
     * 今日回测
     */
    @ApiModelProperty(value = "今日回测")
    private BigDecimal todayRetracement;
}
