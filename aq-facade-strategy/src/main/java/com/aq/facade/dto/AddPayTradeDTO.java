package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:zhangxia
 * @createTime:12:16 2018-2-13
 * @version:1.0
 * @desc:添加支付流水记录dto
 */
@Data
public class AddPayTradeDTO extends BaseValidate{
    private static final long serialVersionUID = 5434328806193930285L;
    /**
     * 支付成功时间
     */
    private Date paySuccessTime;
    /**
     * 第三方订单号
     */
    private String thirdOrderNo;

    /**
     * 交易编码
     */
    private Byte tradeCode;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 支付状态( 0:初始化 1:支付成功 2:支付退款)
     */
    private Byte payState;

    /**
     * 订单号(主订单号)
     */
    private String mainOrderNo;

    /**
     * 创建人id
     */
    private Integer createrId;

    /**
     * 创建人类型 0:系统 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte roleType;
}
