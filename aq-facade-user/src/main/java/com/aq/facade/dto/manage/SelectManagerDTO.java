package com.aq.facade.dto.manage;

import com.aq.core.constant.RoleCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户经理 查询dto
 * @作者： 张霞
 * @创建时间： 19:43 2018/1/21
 * @Copyright @2017 by zhangxia
 */
@Data
public class SelectManagerDTO implements Serializable {

    private static final long serialVersionUID = -5613961765785630145L;

    /**
     * 客户经理表id
     */
    private Integer id;
    /**
     * 用户名-(页面：真实姓名或手机号或ID号)
     */
    private String username;
    /**
     * 电话号码
     */
    private String telphone;
    /**
     * 员工工号-(页面：工号或姓名)
     */
    private String employID;

    /**
     * 角色
     * {@link RoleCodeEnum}
     */
    private Byte roleCode;

    /**
     * 用户名或者电话号码
     */
    private String content;

    /**
     * 用户id
     */
    private Integer userId;
}
