package com.aq.core.constant;

import lombok.Getter;

/**
 * @author:zhangxia
 * @createTime:15:21 2018-2-24
 * @version:1.0
 * @desc:购买方式 1：购买 2：续费
 */
public enum  PurchaseTypeEnum {
    PURCHASE_BUY_ENUM((byte)1,"购买"),
    PURCHASE_RENEW_ENUM((byte)2,"续费");

    PurchaseTypeEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 编码
     */
    @Getter
    private Byte code;
    /**
     * 描述
     */
    @Getter
    private String msg;
}
