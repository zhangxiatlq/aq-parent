package com.aq.facade.dto.account;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhangxia
 * @createTime:16:09 2018-2-23
 * @version:1.0
 * @desc:提现列表查询dto
 */
@Data
public class QueryDrawcashDTO implements Serializable{
    private static final long serialVersionUID = 1830211168021111530L;

    /**
     * 提现申请列表id
     */
    private Integer id;
    /**
     * 客户经理信息（输入姓名或者手机号）
     */
    private String managerMsg;

    /**
     * 审核状态
     */
    private Byte status;

}
