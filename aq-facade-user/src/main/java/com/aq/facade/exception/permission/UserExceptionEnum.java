package com.aq.facade.exception.permission;

import com.aq.util.result.RespCode;
import lombok.Getter;

/**
 * @version 1.0
 * @项目：vulcan-facade-order
 * @描述：
 * @作者： 张霞
 * @创建时间： 18:51 2018/1/21
 * @Copyright @2017 by zhangxia
 */
public enum UserExceptionEnum implements RespCode {
    UPDATE_USER_EXCEPTION("20001","更新用户异常"),
    SELECT_DATA_NOT_EXIST("20002","用户不存在"),
    ADD_MANAGER_FAIL("20003","添加客户经理失败"),
    ADD_MAAGER_EXCEPTION("20004","添加客户经理异常"),
    UPDATE_MANAGER_ADD_RELATION_FAIL("20005","编辑客户经理信息，添加和维护人员关系时失败"),
    UPDATE_MANAGER_EXCEPTION("20006","编辑客户经理信息异常"),
    UPDATE_MANAGER_UPDATE_RELATION_FAIL("20007","编辑客户经理信息，更新和维护人员关系时失败"),
    LUCK_DRAW_FAIL("20008","抽奖失败"),
    ADD_LUCKDRAWNUM_FAIL("20009","初始化抽奖失败"),
    RECEIVEPRIZE_FAIL("20010","初始化抽奖失败"),
    VIP_EXPIRE_GIVE("20011","vip到期赠送购买抽奖机会失败"),
    UPDATE_CLIENT_ADD_RELATION_FAIL("20012","编辑客户信息，添加和客户经理关系时失败"),
    ADD_CUSTOM_ERROR("20012","注册客户异常"),
    CUSTOM_LOGIN_ERROR("20013","客户登录异常")
    ;


    @Getter
    private String code;
    @Getter
    private String msg;

    UserExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
