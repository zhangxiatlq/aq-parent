package com.aq.facade.vo.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhangxia
 * @createTime:12:24 2018-3-2
 * @version:1.0
 * @desc:客户统计数据vo
 */
@Data
public class CustomerStatisticsVO implements Serializable{
    private static final long serialVersionUID = 8361554444265311131L;
    /**
     * 客户总数量
     */
    private Integer allCustomerNum;

    /**
     * 今日新增数量
     */
    private Integer todayAddCustomerNum;

    /**
     * 本周新增数量
     */
    private Integer weekAddCustomerNum;

    /**
     * 本月新增数量
     */
    private Integer monthAddCustomerNum;

    /**
     * 客户vip总数
     */
    private Integer allCustomerNumVIP;

    /**
     * 今日新增vip数量
     */
    private Integer todayAddCustomerNumVIP;

    /**
     * 本周新增vip数量
     */
    private Integer weekAddCustomerNumVIP;

    /**
     * 本月新增vip数量
     */
    private Integer monthAddCustomerNumVIP;


    /**
     * 客户经理总数量
     */
    private Integer allManagerNum;

    /**
     * 今日新增客户经理
     */
    private Integer todayAddManagerNum;

    /**
     * 本周新增客户经理
     */
    private Integer weekAddManagerNum;

    /**
     * 本月新增客户经理
     */
    private Integer monthAddMangerNum;

    /**
     * 微信用户总数量（包含客户和客户经理）
     */
    private Integer allWechaterNum;

    /**
     * 今日新增微信用户（包含客户和客户经理）
     */
    private Integer todayAddWechaterNum;

    /**
     * 本周新增微信用户（包含客户和客户经理）
     */
    private Integer weekAddWechaterNum;

    /**
     * 本月新增微信用户（包含客户和客户经理）
     */
    private Integer monthAddWechaterNum;
}
