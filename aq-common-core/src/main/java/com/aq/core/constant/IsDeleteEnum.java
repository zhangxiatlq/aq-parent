package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：删除枚举
 * @作者： 张霞
 * @创建时间： 12:01 2018/1/20
 * @Copyright @2017 by zhangxia
 */
public enum IsDeleteEnum {
    IS_NOT_DELETE_ENUM((byte)2,"不删除"),
    IS_DELETE_ENUM((byte)1,"删除")
    ;

    IsDeleteEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
