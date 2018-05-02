package com.aq.facade.contant;

import lombok.Getter;

/**
 * 策略推荐状态
 *
 * @author 熊克文
 * @createDate 2018\2\10 0010
 **/
public enum RecommendStateEnum {

    NOT_RECOMMEND((byte)0, "已推荐"),
    RECOMMEND((byte)1, "未推荐");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    RecommendStateEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
