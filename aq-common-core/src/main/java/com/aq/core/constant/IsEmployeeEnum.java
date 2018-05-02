package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：是否是员工枚举 1-是，2-不是
 * @author： 张霞
 * @createTime： 2018/03/21
 * @Copyright @2017 by zhangxia
 */
public enum  IsEmployeeEnum {

    IS_EMPLOYEE_ENUM((byte)1,"是员工"),
    IS_NOT_EMPLOYEE_ENUM((byte)2,"不是员工");
    IsEmployeeEnum(byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private byte code;
    @Getter
    private String msg;
}
