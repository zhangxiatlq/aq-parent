package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询管理员DTO
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class SelectUserDTO implements Serializable{

    private static final long serialVersionUID = 5224642306731373946L;
    /**
     * 工号
     */
    private String employeeID;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 状态
     */
    private Byte isable;
}
