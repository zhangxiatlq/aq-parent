package com.aq.facade.contant;

import lombok.Getter;

/**
 * @author 熊克文
 * @version 1.1
 * @describe description
 * @date 2018/1/20
 * @copyright by xkw
 */
public enum AuditStatusEnum {

    SUCCESS((byte) 1, "通过"),
    FAIL((byte) 2, "未通过"),
    AUDITING((byte) 3, "待审核");

    AuditStatusEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Getter
    private Byte code;
    @Getter
    private String msg;
}
