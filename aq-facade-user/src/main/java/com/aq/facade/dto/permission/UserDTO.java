package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户列表查询dto
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -7565212674308307997L;

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 员工id
     */
    private String employeeID;

    /**
     * 角色类型
     */
    private Byte roleCode;

    /**
     * 用户手机号
     */
    private String telphone;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String msgCode;

    /**
     * 用户状态
     */
    private Byte isable;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
