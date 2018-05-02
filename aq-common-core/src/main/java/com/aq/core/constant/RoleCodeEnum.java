package com.aq.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色code 枚举
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {
    SYSTEM((byte) 0, "系统"),
    ADMIN((byte) 1, "管理员"),
    CUSTOMER((byte) 2, "客户"),
    MANAGER((byte) 3, "经理");

    private Byte code;
    private String message;

    public static RoleCodeEnum getRoleEnumByCode(Byte code) {
        if (null != code) {
            RoleCodeEnum[] roleEnums = RoleCodeEnum.values();
            for (RoleCodeEnum roleEnum : roleEnums) {
                if (code.equals(roleEnum.code)) {
                    return roleEnum;
                }
            }
        }
        return null;
    }
}
