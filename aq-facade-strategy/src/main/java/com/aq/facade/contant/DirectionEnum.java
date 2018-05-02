package com.aq.facade.contant;

import lombok.Getter;

/**
 * 方向枚举
 *
 * @author 熊克文
 * @createDate 2018\2\26 0026
 **/
public enum DirectionEnum {

    SELL((byte) -1, "卖"),
    BUY((byte) 1, "买");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    DirectionEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
