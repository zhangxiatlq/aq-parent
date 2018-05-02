package com.aq.facade.enums;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：更新内容
 * @作者： 张霞
 * @创建时间： 11:36 2018/1/20
 * @Copyright @2017 by zhangxia
 */
public enum  UpdateContentEnum {
    FROZEN((byte)0,"冻结管理员"),
    RESET((byte)1,"重置密码"),
    EDIT((byte)2,"编辑内容"),
    ALLOT_ROLE((byte)3,"分配权限")
    ;


    UpdateContentEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @Getter
    private Byte code;
    @Getter
    private String msg;


}
