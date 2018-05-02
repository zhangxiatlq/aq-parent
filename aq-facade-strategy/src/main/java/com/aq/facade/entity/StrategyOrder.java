package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略订单表
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Table(name = "aq_strategy_order")
@Data
public class StrategyOrder implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 推荐id
     */
    @Column(name = "recommendId")
    private Integer recommendId;

    /**
     * 订单号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 订单状态
     */
    @Column(name = "orderState")
    private Byte orderState;

    /**
     * 支付类型
     */
    @Column(name = "payType")
    private Byte payType;

    /**
     * 购买人id
     */
    @Column(name = "purchaserId")
    private Integer purchaserId;

    /**
     * 购买人类型 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @Column(name = "purchaserType")
    private Byte purchaserType;

    /**
     * 购买时间
     */
    @Column(name = "purchaseTime")
    private Date purchaseTime;

    /**
     * 购买价格
     */
    @Column(name = "purchasePrice")
    private BigDecimal purchasePrice;

    /**
     * 购买金额
     */
    @Column(name = "purchaseMoney")
    private BigDecimal purchaseMoney;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 策略Id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

    /**
     * 主订单号 如果是主订单 则没有
     */
    @Column(name = "mainOrderNo")
    private String mainOrderNo;
}