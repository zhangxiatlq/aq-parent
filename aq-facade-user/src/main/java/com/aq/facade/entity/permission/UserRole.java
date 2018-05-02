package com.aq.facade.entity.permission;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：用户角色关联表
 * @作者： 张霞
 * @创建时间： 17:00 2018/1/19
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "aq_permission_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3211929214554570870L;

    @Id
    private Integer id;
    /**
     * 用户id
     */
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "roleId")
    private Integer roleId;
}
