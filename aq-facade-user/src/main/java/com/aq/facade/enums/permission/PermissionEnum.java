package com.aq.facade.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 权限枚举
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Getter
@AllArgsConstructor
public enum PermissionEnum {
    /**
     * 是否可用
     */
    YES_USE((byte) 1, "可用"),
    NO_USE((byte) 0, "不可用"),
    YES_DEFAULT((byte) 0, "默认分配权限"),
    NO_DEFAULT((byte) 1, "未默认分配权限"),
    YES_BASE_MENU((byte) 0, "基础菜单"),
    NO_BASE_MENU((byte) 1, "服务菜单"),
    NO_DELETE((byte) 2, "未删除"),
    YES_DELETE((byte) 1, "删除");

    private Byte code;

    private String message;
}
