package com.aq.facade.dto;

import lombok.Data;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 策略推送VO
 * @date 2018/1/25
 * @copyright by xkw
 */
@Data
public class StrategysPushQueryDTO {

    /**
     * 姓名
     */
    private String realName;

    /**
     * 策略id
     */
    private String strategysId;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 账号类型 {@link com.aq.core.constant.UserTypeEnum}
     */
    private String roleType;
}
