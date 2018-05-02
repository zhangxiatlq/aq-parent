package com.aq.facade.contant;

import lombok.Getter;

public enum PushStateEnum {
    PUSH((byte)0, "开启"),
    NOT_PUSH((byte)1, "关闭");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    PushStateEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
