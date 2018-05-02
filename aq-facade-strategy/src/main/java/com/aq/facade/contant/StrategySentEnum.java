package com.aq.facade.contant;

import lombok.Getter;

/**
 * @author:zhangxia
 * @createTime:12:21 2018-2-12
 * @version:1.0
 * @desc:客户经理是否给某个客户推送过
 */
public enum  StrategySentEnum {
    SENT((byte)0, "已推荐"),
    NOT_SENT((byte)1, "未推荐");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    StrategySentEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
