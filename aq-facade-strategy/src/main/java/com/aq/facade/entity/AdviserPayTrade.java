package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾支付流水表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_pay_trade")
@Data
public class AdviserPayTrade implements Serializable {

    private static final long serialVersionUID = 3051997848569277286L;
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 支付成功时间
     */
    @Column(name = "paySuccessTime")
    private Date paySuccessTime;

    /**
     * 第三方订单号
     */
    @Column(name = "thirdOrderNo")
    private String thirdOrderNo;

    /**
     * 交易编码
     */
    @Column(name = "tradeCode")
    private Byte tradeCode;

    /**
     * 支付金额
     */
    @Column(name = "payMoney")
    private BigDecimal payMoney;

    /**
     * 支付状态( 0:初始化 1:支付成功 2:支付退款)
     */
    @Column(name = "payState")
    private Byte payState;

    /**
     * 订单号(主订单号)
     */
    @Column(name = "mainOrderNo")
    private String mainOrderNo;

    /**
     * 创建人id
     */
    @Column(name = "createrId")
    private Integer createrId;

    /**
     * 创建人类型 0:系统 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @Column(name = "roleType")
    private Byte roleType;
}