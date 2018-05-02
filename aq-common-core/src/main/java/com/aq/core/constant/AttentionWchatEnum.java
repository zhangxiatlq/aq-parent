package com.aq.core.constant;

import lombok.Getter;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：微信是否已关注
 * @author： 张霞
 * @createTime： 2018/03/20
 * @Copyright @2017 by zhangxia
 */
public enum  AttentionWchatEnum {
    ATTENTION_WCHAT_ENUM((byte)2, "已关注"),
    NOT_ATTENTION_WCHAT_ENUM((byte)1, "未关注");

    @Getter
    private Byte code;
    @Getter
    private String desc;

    AttentionWchatEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
