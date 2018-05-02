package com.aq.facade.vo;

import lombok.Data;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 策略推送VO
 * @date 2018/1/25
 * @copyright by xkw
 */
@Data
public class StrategysPushQueryVO {

    /**
     * 姓名
     */
    private String realName;

    /**
     * id
     */
    private Integer id;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 账号类型 {@link com.aq.core.constant.UserTypeEnum}
     */
    private Byte roleType;

    /**
     * 推送id集合
     */
    private String pushIds;
}
