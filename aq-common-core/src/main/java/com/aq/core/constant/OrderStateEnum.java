package com.aq.core.constant;

import lombok.Getter;

/**
 * @author:zhangxia
 * @createTime:9:45 2018-2-13
 * @version:1.0
 * @desc:订单状态枚举
 */
public enum OrderStateEnum {
    PAYING((byte)1,"支付中"),
    FAIL((byte)2,"支付失败"),
    SUCCESS((byte)3,"支付成功");

    OrderStateEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private Byte code;
    @Getter
    private String msg;
}
