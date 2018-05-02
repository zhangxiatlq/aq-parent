package com.aq.facade.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 安全设置基础项
 *
 * @author 郑朋
 * @create 2018/3/23
 **/
@Getter
@AllArgsConstructor
public enum SecurityBaseSettingEnum {
    /**
     * 安全设置基础项
     */
    CUSTOMER_TEL_PHONE(1, "客户列表手机号"),
    ALL_MANAGER(2, "所有客户经理"),
    ALL_USER_DATA(3, "所有用户数据"),
    MANAGER_TEL_PHONE(4, "客户经理列表手机号");
    private Integer id;

    private String desc;
}
