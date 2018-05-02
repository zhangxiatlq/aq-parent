package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改登录密码
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class UpdatePassDTO implements Serializable {

    private static final long serialVersionUID = -3060582282802990879L;

    /**
     * 原始密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 用户id
     */
    private Integer id;

}
