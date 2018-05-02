package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新订单
 *
 * @author 郑朋
 * @create 2018/3/7
 */
@Data
public class AdviserUpdateOrderDTO implements Serializable{

    private static final long serialVersionUID = 7241992826149205598L;
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
