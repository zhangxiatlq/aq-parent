package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 策略购买表
 *
 * @author 熊克文
 * @date 2018-02-08
 */

@Table(name = "aq_strategy_purchase")
@Data
public class StrategyPurchase implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 策略id
     */
    @Column(name = "strategyId")
    private Integer strategyId;

    /**
     * 到期时间
     */
    @Column(name = "expiryTime")
    private Date expiryTime;

    /**
     * 购买人id
     */
    @Column(name = "purchaserId")
    private Integer purchaserId;

    /**
     * 购买人类型 2:客户 3 经理
     */
    @Column(name = "purchaserType")
    private Byte purchaserType;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

}