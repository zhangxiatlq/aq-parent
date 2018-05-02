package com.aq.facade.entity.account;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:15:46 2018-2-23
 * @version:1.0
 * @desc:提现申请 实体
 */
@Data
@Table(name = "aq_drawcash_apply")
public class DrawcashApply extends BaseEntity{

    private static final long serialVersionUID = -9135664047657007948L;

    /**
     * 提现金额
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 银行卡号
     */
    @Column(name = "bankNo")
    private String bankNo;

    /**
     * 提现状态(1=审核中；2=已通过；3=未通过)
     * {@link com.aq.core.constant.CashAuthStatusEnum}
     */
    @Column(name = "status")
    private Byte status;

    /**
     * 提现ip地址
     */
    @Column(name = "requestIp")
    private String requestIp;

    /**
     * 订单号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     *是否删除：2、否 1、是
     * {@link com.aq.core.constant.IsDeleteEnum}
     */
    @Column(name = "isDelete")
    private Byte isDelete;

    /**
     * 提现银行
     */
    @Column(name = "bankName")
    private String bankName;

    /**
     * 开户网点
     */
    @Column(name = "openingAddress")
    private String openingAddress;

    /**
     * 审核描述
     */
    @Column(name = "authDesc")
    private String authDesc;


}
