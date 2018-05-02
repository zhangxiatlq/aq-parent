package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhangxia
 * @createTime: 2018/4/26 11:00
 * @version:1.0
 * @desc:工具收益率表
 */
@Data
@Table(name = "aq_tool_income_rate")
public class ToolIncomeRate implements Serializable{

    private static final long serialVersionUID = 7515341021328126915L;

    @Id
    private Integer id;

    /**
     * 买入日期
     */
    @Column(name = "toBuyDate")
    private Date toBuyDate;

    /**
     * 卖出日期
     */
    @Column(name = "toSellDate")
    private Date toSellDate;

    /**
     * 收益率
     */
    @Column(name = "incomeRate")
    private Double incomeRate;

    /**
     * 股票代码
     */
    @Column(name = "stockCode")
    private String stockCode;

    /**
     * 买入推送记录id
     */
    @Column(name = "buyPushId")
    private Integer buyPushId;

    /**
     * 卖出推送记录id
     */
    @Column(name = "sellPushId")
    private Integer sellPushId;

    /**
     * 用户id（客户或者客户经理表的id）
     */
    @Column(name = "userId")
    private Integer userId;

    /**
     * 用户类型（2-客户；3-客户经理）
     * @see com.aq.core.constant.RoleCodeEnum
     */
    @Column(name = "userType")
    private Byte userType;

    /**
     * 工具绑定id
     */
    @Column(name = "bindId")
    private Integer bindId;

    /**
     * 趋势量化工具表中专业工具的id
     */
    @Column(name = "toolId")
    private Integer toolId;
}
