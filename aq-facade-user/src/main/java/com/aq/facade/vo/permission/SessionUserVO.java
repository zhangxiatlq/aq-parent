package com.aq.facade.vo.permission;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * SessionUserVO
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class SessionUserVO implements Serializable {

    private static final long serialVersionUID = 6829940553030159312L;

    /**
     * 用户登录表id
     */
    private Integer id;


    /**
     * 用户名
     */
    private String userName;

    /**
     * 员工号(登陆账号)
     */
    private String employeeID;


    /**
     * 电话号码
     */
    private String telphone;


    /**
     * 登陆账户对应的角色
     */
    List<SessionRoleVO> sessionRoleVo;

}
