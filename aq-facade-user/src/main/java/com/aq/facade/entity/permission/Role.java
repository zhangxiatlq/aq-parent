package com.aq.facade.entity.permission;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：后台角色表实体
 * @作者： 张霞
 * @创建时间： 16:59 2018/1/19
 * @Copyright @2018 by zhangxia
 */
@Data
@Table(name = "aq_permission_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 8815468090259167070L;


    /**
     * 角色名称
     */
    @Column(name = "roleName")
    private String roleName;
    /**
     * 角色编码
     */
    @Column(name = "roleCode")
    private Byte roleCode;
    /**
     * 是否可用：0不可用(冻结)   1 可用(未冻结)
     */
    @Column(name = "status")
    private Byte status;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 是否默认分配权限 0-默认 1-非默认
     */
    @Column(name = "isDefault")
    private Byte isDefault;
}
