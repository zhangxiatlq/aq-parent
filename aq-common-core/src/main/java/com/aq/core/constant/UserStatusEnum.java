package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：管理员用户状态
 * @作者： 张霞
 * @创建时间： 12:08 2018/1/20
 * @Copyright @2017 by zhangxia
 */
public enum UserStatusEnum {
    NORMAL_ENUM((byte)1,"正常"),
    FORZEN_ENUM((byte)0,"冻结")
    ;

    UserStatusEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
