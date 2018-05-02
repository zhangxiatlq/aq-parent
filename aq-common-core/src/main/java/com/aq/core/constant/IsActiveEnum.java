package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-facade-core
 * @描述：0 为不活跃 1为活跃 is_active为0时不能登录
 * @作者： 张霞
 * @创建时间： 14:46 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public enum IsActiveEnum {
    IS_SACTIVE_ENUM((byte)0,"不活跃"),
    IS_NOT_ACTIVE_ENUM((byte)1,"活跃")
    ;

    IsActiveEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
