package com.aq.facade.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: CustomerEnum
 * @Description: 客户相关枚举
 * @author: lijie
 * @date: 2018年2月8日 下午12:27:32
 */
@AllArgsConstructor
@Getter
public enum CustomerEnum {
    YES_DELETE((byte) 1, "已删除"),
    NO_DELETE((byte) 2, "未删除"),
    YES_VIP((byte) 1, "是"),
    NOT_VIP((byte) 2, "否"),
    YES_BIND_WEB_CHAT((byte) 1, "绑定微信"),
    NO_BIND_WEB_CHAT((byte) 2, "未绑定微信");

    private Byte status;
    private String desc;
}
