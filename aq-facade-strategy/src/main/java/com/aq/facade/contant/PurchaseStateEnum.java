package com.aq.facade.contant;

import lombok.Getter;

/**
 * 策略购买状态
 *
 * @author 熊克文
 * @createDate 2018\2\10 0010
 **/
public enum PurchaseStateEnum {

    PURCHASE((byte)0, "已购买"),
    NOT_PURCHASE((byte)1, "未购买"),
    PURCHASE_MYSELF((byte)2, "自己创建的");//用于投顾自己创建的不需要进行购买

    @Getter
    private Byte code;
    @Getter
    private String desc;

    PurchaseStateEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
