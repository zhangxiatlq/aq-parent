package com.aq.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易码枚举
 *
 * @author 郑朋
 * @create 2018/2/23
 */
@Getter
@AllArgsConstructor
public enum TransCodeEnum {

    BALANCE_DRAW_CASH(1001, "余额提现", -1, "余额提现"),
    BALANCE_DRAW_CASH_BACK(1002, "余额提现退回", 1, "余额提现失败返回余额"),
    BALANCE_VIP_CASH(1003, "vip充值到账", 1, "vip充值到账"),
    MANAGER_BUY_MD(1004, "购买策略", -1, "客户经理购买策略"),
    CUSTOMER_BUY_MD(1005, "购买策略", -1, "客户购买策略"),
    CUSTOMER_MANAGER_BUY_MD(1006, "购买策略到账", 1, "客户向客户经理购买策略"),
    ADVISER_MANAGER_BUY(1007, "购买投顾", -1, "客户经理购买投顾"),
    ADVISER_CUSTOMER_BUY(1008, "购买投顾", -1, "客户购买投顾"),
    ADVISER_CUSTOMER_MANAGER_BUY(1009, "购买投顾到账", 1, "客户向客户经理购买投顾"),
    ADVISER_MANAGER_MANAGER_BUY(1010, "购买投顾到账", 1, "客户经理向客户经理购买投顾"),
    USER_BALANCE_SETTLE(1011,"余额结算",-1,"员工余额结算")
    ;

    private int code;
    private String source;
    private int mark;
    private String msg;
}
