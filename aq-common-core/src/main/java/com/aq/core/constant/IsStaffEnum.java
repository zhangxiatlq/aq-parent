package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-commom-core
 * @描述：是否是员工,0不是员工,1是员工
 * @作者： 张霞
 * @创建时间： 14:39 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public enum IsStaffEnum {
    IS_NOT_STAFF_ENUM((byte)0,"不是员工"),
    IS_STAFF_ENUM((byte)1,"是员工")
    ;

    IsStaffEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;
}
