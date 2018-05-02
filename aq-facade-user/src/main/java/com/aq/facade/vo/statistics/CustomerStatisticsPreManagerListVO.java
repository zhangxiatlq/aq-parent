package com.aq.facade.vo.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhangxia
 * @createTime:15:36 2018-3-2
 * @version:1.0
 * @desc:分别统计客户经理下面的客户数量
 */
@Data
public class CustomerStatisticsPreManagerListVO implements Serializable {
    private static final long serialVersionUID = -3234784146239987986L;

    /**
     * 客户经理id
     */
    private Integer id;
    /**
     * id号
     */
    private String idCode;

    /**
     * 客户经理电话号码
     */
    private String telphone;

    /**
     * 客户经理姓名
     */
    private String realName;

    /**
     * 新增客户经理
     */
    private Integer addManagerNum;

    /**
     * 客户经理总数
     */
    private Integer allManagerNum;

    /**
     * 新增客户
     */
    private Integer addCustomerNum;

    /**
     * 总客户数量
     */
    private Integer cumstomerNum;

    /**
     * 当日新增vip数量
     */
    private Integer addVIPNum;
    /**
     * 客户的vip数量
     */
    private Integer vipNum;

    /**
     * 客户经理 维护的客户新增VIP数量
     */
    private Integer addVipNum;

    /**
     * 开通微信的数量（既绑定微信又关注微信的数量）
     */
    private Integer openNum;

    /**
     * 取消关注微信数量
     */
    private Integer cancelOpenNum;

    /**
     * 昨日增加微信关注数据--新增微信数量（既绑定微信又关注微信的数量）
     */
    private Integer addAttentionNum;

    /**
     * 昨日取消微信关注数据--新增取消关注数量
     */
    private Integer cancelAttentionNum;
    /**
     * 昨日关注微信变化数量
     */
    private Integer attentionChangeNum;

    /**
     * 是否是员工枚举 1-是，2-不是
     * @see  com.aq.core.constant.IsEmployeeEnum
     */
    private Byte isEmployee;

}
