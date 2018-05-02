package com.aq.facade.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 
 * @ClassName: AdviserTradeRecordVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月13日 上午10:02:00
 */
@Data
public class AdviserTradeRecordVO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 5960435401305816488L;

	/**
     * 主键id
     */
    private Integer id;

    /**
     * 投顾id
     */
    private Integer adviserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 指标时间
     */
    private Date indexTime;

    /**
     * 创建人
     */
    private Integer createId;

    /**
     * 累计收益(策略从开始那天累积的收益率，我的收益，百分比数值)
     */
    private String cumulativeIncome;

    /**
     * 年化收益
     */
    private String annualIncome;

    /**
     * 最大回撤（每天的收益情况和这天之前的最大值的一个比值，称为回撤，所有回撤里最大的一个值即最大回撤）
     */
    private String maxRetracement;

    /**
     * 收益回撤比（最大回撤和年化收益的比值）
     */
    private String returnRetracementRatio;

    /**
     * 投顾超过同期基准
     */
    private String adviserPeriod;

    /**
     * 夏普率（单位风险所产生的收益）
     */
    private String sharpRate;

    /**
     * 总资产（策略目前的所有资产）
     */
    private String totalAssets;

    /**
     * 可用资产（改为可用资金，即手上的现金）
     */
    private String availableAssets;

    /**
     * 参考市值（手上股票的按当日收盘价计算的总市值）
     */
    private String marketValue;

    /**
     * 今日收益率
     */
    private String todayRate;

    /**
     * 基准指数值（基准指数的数值，百分比数值）
     */
    private String numericValue;

    /**
     * 波动率
     */
    private String volatility;

    /**
     * 今日回测
     */
    private String todayRetracement;
    /**
     * 初始资产
     */
    private String initTotalPrice;
    /**
     * 冻结资金
     */
    private String totalFreezeNum;
}
