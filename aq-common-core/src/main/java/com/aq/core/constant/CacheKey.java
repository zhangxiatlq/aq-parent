package com.aq.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: CacheKey
 * @Description: 缓存key
 * @author: lijie
 * @date: 2018年2月27日 下午3:54:00
 */
@Getter
@AllArgsConstructor
public enum CacheKey {

    /**
     * 股票分组key
     */
    STOCK_GROUP("STOCK_CODE_TIME"),
    /**
     * 网格工具基础数据分组KEY
     */
    GRID_TOOL_BASE("GRID_TOOL_BASE"),
    /**
     * 卖点工具基础数据分组KEY
     */
    SELLING_TOOL_BASE("SELLING_TOOL_BASE"),
    /**
     * 趋势量化工具基础数据分组KEY
     */
    TREND_TOOL_BASE("TREND_TOOL_BASE"),

    /**
     * 专用趋势量化工具基础数据分组KEY
     */
    SPECIAL_TREND_TOOL_BASE("SPECIAL_TREND_TOOL_BASE"),
    /**
     * 网格工具计算数据分组KEY
     */
    GRID_TOOL_CALCULATE("GRID_TOOL_CALCULATE"),
    /**
     * 卖点工具计算数据分组KEY
     */
    SELLING_TOOL_CALCULATE("SELLING_TOOL_CALCULATE"),
    /**
     * 历史股票信息分组key
     */
    TREND_TOOL_CALCULATE_BASE("TREND_TOOL_CALCULATE_BASE"),

    /**
     * 专用历史股票信息分组key
     */
    SPECIAL_TREND_TOOL_CALCULATE_BASE("SPECIAL_TREND_TOOL_CALCULATE_BASE"),

    /**
     * 投顾指标记录 分组key
     */
    ADVISER_TRADE_RECORD("ADVISER_TRADE_RECORD"),

    /**
     * 投顾指标记录 分组key
     */
    ADVISER_TRADE_RECORD_REALTIME("ADVISER_TRADE_RECORD_REALTIME"),

    /**
     * 投顾指标记录  分组key
     */
    DAILY_ADVISER_TRADE_RECORD("DAILY_ADVISER_TRADE_RECORD"),

    /**
     * 持仓 分组key
     */
    ADVISER_POSITION_UPDATE("ADVISER_POSITION_UPDATE"),

    /**
     * 持仓 分组key
     */
    ADVISER_POSITION_UPDATE_REALTIME("ADVISER_POSITION_UPDATE_REALTIME"),

    /**
     * 持仓 分组key
     */
    ADVISER_POSITION("ADVISER_POSITION"),

    /**
     * 委托记录 分组key
     */
    INVESTMENT_CONSIGNMENT("INVESTMENT_CONSIGNMENT"),

    /**
     * 委托记录-推送 分组key
     */
    INVESTMENT_CONSIGNMENT_PUSH("INVESTMENT_CONSIGNMENT_PUSH"),

    /**
     * 交易时间
     */
    TRADE_DATE("TRADE_DATE"),

    /**
     * 工作日
     */
    TRADE_DAY("TRADE_DAY"),

    /**
     * 股票实时数据
     */
    STOCK_REALTIME_DATA("STOCK_REALTIME_DATA");

    private String key;
}
