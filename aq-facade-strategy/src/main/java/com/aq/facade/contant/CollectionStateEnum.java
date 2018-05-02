package com.aq.facade.contant;

import lombok.Getter;

/**
 * 策略收藏状态
 *
 * @author 熊克文
 * @createDate 2018\2\10 0010
 **/
public enum CollectionStateEnum {

    COLLECTION((byte)0, "已收藏"),
    NOT_COLLECTION((byte)1, "未收藏");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    CollectionStateEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
