package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-facade-core
 * @描述：是否是超级管理员 0 不是 1 是
 * @作者： 张霞
 * @创建时间： 14:49 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public enum IsSuperuserEnum {
    IS_NOT_SUPERUSER_ENUM((byte)0,"不是超级管理员"),
    IS_SUPERUSER_ENUM((byte)1,"是超级管理员")
    ;

    IsSuperuserEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
