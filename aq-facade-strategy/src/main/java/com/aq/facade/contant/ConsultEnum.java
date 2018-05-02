package com.aq.facade.contant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 今日汇 枚举
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Getter
@AllArgsConstructor
public enum ConsultEnum {
    /**
     * 是否可见
     */
    YEW_VISIBLE((byte) 1,"可见"),
    NO_VISIBLE((byte) 2,"不可见")
    ;

    private Byte code;
    private String desc;
}
