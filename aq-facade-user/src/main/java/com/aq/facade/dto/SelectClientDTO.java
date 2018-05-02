package com.aq.facade.dto;

import com.aq.core.constant.RoleCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户翻页列表查询
 * @作者： 张霞
 * @创建时间： 17:24 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
public class SelectClientDTO implements Serializable{

    private static final long serialVersionUID = 8668125074587991611L;

    /**
     * 客户用户id
     */
    private Integer id;
    /**
     * 客户用户名
     */
    private String userName;

    /**
     * 客户电话
     */
    private String telphone;
    /**
     * 客户经理id号
     */
    private Integer staffnum;

    /**
     * 工号
     */
    private String employeeID;

    /**
     * 角色
     * {@link RoleCodeEnum}
     */
    private Byte roleCode;

    /**
     * 员工id
     */
    private Integer userId;
}
