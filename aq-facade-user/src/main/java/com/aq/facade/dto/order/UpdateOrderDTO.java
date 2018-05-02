package com.aq.facade.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhangxia
 * @createTime:14:31 2018-2-13
 * @version:1.0
 * @desc:更新订单dto
 */
@Data
public class UpdateOrderDTO implements Serializable{
    private static final long serialVersionUID = 1379833457942905545L;

    /**
     * 订单状态
     */
    private Byte orderState;
    /**
     * 支付方式
     */
    private Byte payType;
    /**
     * 主订单号
     */
    private String mainOrderNo;
    /**
     * 子订单号
     */
    private String orderNo;
    /**
     * 订单表id
     */
    private Integer id;
}
