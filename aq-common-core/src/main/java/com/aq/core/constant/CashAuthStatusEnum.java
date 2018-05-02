package com.aq.core.constant;

import lombok.Getter;

public enum CashAuthStatusEnum {
    //1=审核中；2=已通过；3=未通过
    CASH_AUTHING_ENUM((byte)1,"审核中"),
    CASH_ACCESS_ENUM((byte)2,"审核通过"),
    CASH_REJECT_ENUM((byte)3,"未通过"),
    ;


    CashAuthStatusEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private Byte code;
    @Getter
    private String msg;
}
