package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-commom-core
 * @描述：精品策略类型
 * @作者： 张霞
 * @创建时间： 15:19 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public enum BoutiqueTypeEnum {
    //0位系统策略 1为用户自定义策略 2为用户引导式策略，3待定，4客户经理策略
    BOUTIQUE_SYSTEM_ENUM((byte)0,"平台策略"),
    BOUTIQUE_CUSTOMIZE_ENUM((byte)1,"用户自定义策略"),
    BOUTIQUE_GUILD_ENUM((byte)2,"用户引导式策略"),
    BOUTIQUE_UNKNOW_ENUM((byte)3,"待定"),
    BOUTIQUE_MANAGER_ENUM((byte)4,"客户经理策略")
    ;

    BoutiqueTypeEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
