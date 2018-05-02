package com.aq.facade.entity.account;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户流水
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
@Table(name = "aq_account_flow")
@Data
public class AccountFlow implements Serializable {

    private static final long serialVersionUID = 8592758861469918375L;

    @Id
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "orderNo")
    private String orderNo;

    /**
     * 流水单号
     */
    @Column(name = "flowNo")
    private String flowNo;

    /**
     * 金额
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 交易编码类型
     */
    @Column(name = "tradeCode")
    private Integer tradeCode;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 第三方交易单号
     */
    @Column(name = "thirdTranCode")
    private String thirdTranCode;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "createrId")
    private Integer createrId;

    /**
     * 账户类型 1-管理员（员工） 2-客户 3-客户经理
     *
     * @see com.aq.core.constant.RoleCodeEnum
     */
    @Column(name = "roleType")
    private Byte roleType;

    /**
     * 客户电话
     */
    @Column(name = "customerTelphone")
    private String customerTelphone;

    /**
     * 客户经理工号
     */
    @Column(name = "idCode")
    private Integer idCode;

    /**
     * 客户经理余额
     */
    @Column(name = "managerBalance")
    private BigDecimal managerBalance;

    /**
     * 员工工号
     */
    @Column(name = "employeeID")
    private String employeeID;

    /**
     * 员工余额
     */
    @Column(name = "userBalance")
    private BigDecimal userBalance;

    /**
     * AQ分成金额
     */
    @Column(name = "aqDivide")
    private BigDecimal aqDivide;

    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 客户id
     */
    @Column(name = "customerId")
    private Integer customerId;

    /**
     * 员工id
     */
    @Column(name = "userId")
    private Integer userId;

    /**
     * 客户金额
     */
    @Column(name = "customerBalance")
    private BigDecimal customerBalance;
}
