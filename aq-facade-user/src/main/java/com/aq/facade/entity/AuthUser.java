package com.aq.facade.entity;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：用户(客户和客户经理)表
 * @作者： 张霞
 * @创建时间： 15:15 2018/1/18
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "auth_user")
public class AuthUser implements Serializable {

    private static final long serialVersionUID = -5173723389723564118L;

    @Id
    private Integer id;
    
    @Column(name = "password")
    private String password;
    @Column(name = "last_login")
    private Date lostLogin;

    /**
     * 是否是超级管理员 0 不是 1 是
     * {@link com.aq.core.constant.IsSuperuserEnum}
     */
    @Column(name = "is_superuser")
    private Byte isSuperuser;
    @Column(name = "username")
    private String username = "";
    @Column(name = "first_name")
    private String firstName = "";
    @Column(name = "last_name")
    private String lastName = "";
    @Column(name = "email")
    private String email = "";
    /**
     * 0不是员工,1是员工
     * {@link com.aq.core.constant.IsStaffEnum}
     */
    @Column(name = "is_staff")
    private Byte isStaff;
    /**
     * 0 为不活跃 1为活跃 is_active为0时不能登录--默认1
     * {@link com.aq.core.constant.IsActiveEnum}
     */
    @Column(name = "is_active")
    private Byte isActive;
    @Column(name = "date_joined")
    private Date dateJoined;
}
