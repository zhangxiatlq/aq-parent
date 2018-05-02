package com.aq.facade.dto;

import lombok.Data;

/**
 * @author: zhangxia
 * @desc: 策略推送
 * @params:
 * @methodName:
 * @date: 2018/3/28 0028 上午 10:59
 * @version:2.1.2
 */
@Data
public class AdviserPushQueryDTO {

    /**
     * 姓名
     */
    private String realName;

    /**
     * 投顾id
     */
    private String adviserId;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 账号类型 {@link com.aq.core.constant.UserTypeEnum}
     */
    private String roleType;
}
