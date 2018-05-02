package com.aq.facade.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分组 enum
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Getter
@AllArgsConstructor
public enum GroupEnum {
    YES_DELETE((byte)1,"已删除"),
    NO_DELETE((byte)2,"未删除"),
    YES_DEFAULT((byte)1,"默认"),
    NO_DEFAULT((byte)2,"非默认"),
    DEFALUT_NAME((byte)0,"我的好友")
    ;
    @Getter
    private Byte status;

    @Getter
    private String desc;
}
