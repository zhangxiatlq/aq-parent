package com.aq.core.constant;

import lombok.Getter;

/**
 * 股票交易方向美剧
 *
 * @author 熊克文
 * @createDate 2018\2\8 0008
 **/
public enum SharesDirectionEnum {

    /**
     * 卖
     **/
    SELL((byte) 0, "卖"),
    BUY((byte) 1, "买");

    SharesDirectionEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private Byte code;
    @Getter
    private String msg;
}
