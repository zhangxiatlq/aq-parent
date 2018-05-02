package com.aq.core.authentication.dto;

import lombok.Data;


/**
 * 个人信息认真
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 */
@Data
public class PersonalAuthDTO {

    /**
     * 银行卡号
     */
    private String bankCard;
    /**
     * 身份证号
     */
    private String cardNo;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;
}
