package com.aq.facade.contant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 投顾枚举
 *
 * @author 郑朋
 * @create 2018/3/6 0006
 **/
@Getter
@AllArgsConstructor
public enum AdviserEnum {
    /**
     * 是否导入
     */
    YES_IMPORT((byte) 1, "已导入"),
    NOT_IMPORT((byte) 2, "未导入"),

    YES_TRADE((byte) 1, "产生交易"),
    NO_TRADE((byte) 2, "未产生交易"),
    TRADE_DEAL((byte) 1, "已成"),
    TRADE_UN_DEAL((byte) 2, "未成"),
    TRADE_CANCEL((byte) 3, "已撤"),

    /**
     * 时间配置枚举
     */

    SHANGHAI_STOCK((byte) 1, "上海交易所"),
    SHENZHEN_STOCK((byte) 2, "深圳交易所"),
    TRADE_TIME((byte) 1, "交易时间"),
    BIDDING_TIME((byte) 2, "竞价时间"),
    ENTRUST_TIME((byte) 3, "委托时间"),
    CANCEL_TIME((byte) 4, "撤单时间"),

    /**
     * 投顾是否显示
     */
    YES_VISIBLE((byte) 1, "显示"),
    NO_VISIBLE((byte) 2, "不显示");

    private Byte code;
    private String desc;


}
