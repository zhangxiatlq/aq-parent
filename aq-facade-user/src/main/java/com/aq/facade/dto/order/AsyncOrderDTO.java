package com.aq.facade.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 异步回调订单DTO
 *
 * @author 郑朋
 * @create 2018/2/26 0026
 **/
@Data
public class AsyncOrderDTO implements Serializable {
    private static final long serialVersionUID = -3914368258077074148L;

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
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 第三方支付流水
     */
    private String thirdOrderNo;
}
