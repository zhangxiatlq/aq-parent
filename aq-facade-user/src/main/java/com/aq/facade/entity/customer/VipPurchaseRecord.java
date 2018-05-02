package com.aq.facade.entity.customer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * vip购买流水表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_vip_purchase_record")
public class VipPurchaseRecord implements Serializable {
    private static final long serialVersionUID = 3604600525296918864L;


    @Id
    private Integer id;

    /**
     * 购买人id
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 购买时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 支付金额
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 订单id
     */
    @Column(name = "orderId")
    private Integer orderId;

    /**
     * 订单号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 第三方支付流水号
     */
    @Column(name = "thirdOrderNo")
    private String thirdOrderNo;


    /**
     * 购买状态（支付回调状态） 1-成功 2-失败
     */
    @Column(name = "status")
    private Byte status;
}