package com.aq.facade.vo.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UserVO
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -5195280967436667731L;

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
     * 状态 1 启用 2 禁用
     */
    private Byte isable;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
