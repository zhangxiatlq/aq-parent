package com.aq.facade.exception.customer;

import com.aq.util.result.RespCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户异常枚举
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Getter
@AllArgsConstructor
public enum CustomerExceptionEnum implements RespCode {

    /**
     * 分组模块
     */
    GROUP_NUM_EXIST("10800", "分组数量已经超过10个"),
    GROUP_NAME_EXIST("10801", "分组名称已经存在"),
    UPDATE_LOGINPWD_ERROR("10802","修改/找回客户登录密码异常"),
    UPDATE_CUSTOMER_ERROR("10803","修改客户异常"),
    DEFAULT_NOT_UPDATE("10804","默认分组不能修改"),
    NOT_EXISTS("10805","客户不存在"),
    UPDATE_CUSTOMER_OPENID_ERROR("10806","绑定客户微信openId异常"),
    ACCOUNT_ALREADY_BIND("10807","账号已绑定其它微信"),
    OPENID_EXISTS("10808","微信已绑定其它账号")
    ;

    private String code;
    private String msg;
}
