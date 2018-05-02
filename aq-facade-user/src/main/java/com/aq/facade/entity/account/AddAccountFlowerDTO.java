package com.aq.facade.entity.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增账户流水DTO
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
public class AddAccountFlowerDTO implements Serializable {
    private static final long serialVersionUID = 2722971070273023087L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte roleType;

    /**
     * 交易编码
     */
    private Integer tradeCode;

    /**
     * 创建人id
     */
    private Integer createrId;

    /**
     * 第三方支付流水
     */
    private String thirdTranCode;

    /**
     * 客户电话
     */
    private String customerTelphone;

    /**
     * 客户经理工号
     */
    private Integer idCode;

    /**
     * 客户经理余额
     */
    private BigDecimal managerBalance;

    /**
     * 员工工号
     */
    private String employeeID;

    /**
     * 员工余额
     */
    private BigDecimal userBalance;

    /**
     * AQ分成金额
     */
    private BigDecimal aqDivide;

    /**
     * 客户经理id
     */
    private Integer managerId;

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 员工id
     */
    private Integer userId;

    /**
     * 客户金额
     */
    private BigDecimal customerBalance;
}
