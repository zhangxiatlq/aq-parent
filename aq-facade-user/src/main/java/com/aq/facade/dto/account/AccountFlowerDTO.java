package com.aq.facade.dto.account;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户经理余额流水DTO
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
@Data
public class AccountFlowerDTO implements Serializable{
    private static final long serialVersionUID = -849645083349884194L;

    /**
     * 账户id（客户/客户经理 id/员工id）
     */
    private Integer accountId;

    /**
     * 账户类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte roleType;

    /**
     * 流水单号
     */
    private String flowNo;

    /**
     * 客户手机号
     */
    private String customerTelphone;

    /**
     * 创建时间
     */
    private String createTimeStart;

    /**
     * 创建时间
     */
    private String createTimeEnd;

    private Integer managerId;
    private Integer userId;
    private Integer customerId;

    /**
     * 客户经理 查询条件（包含：真实姓名或者手机或者工号）
     */
    private String managerMsg;

    /**
     * 维护人员 查询条件（包含：姓名或工号）
     */
    private String userMsg;

    /**
     * 客户 查询条件（包含：手机号或者真实姓名）
     */
    private String customerMsg;

}
