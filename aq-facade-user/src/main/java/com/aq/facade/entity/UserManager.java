package com.aq.facade.entity;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：维护员与客户经理关联表
 * @作者： 张霞
 * @创建时间： 13:47 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "aq_user_manager")
public class UserManager extends BaseEntity{
    private static final long serialVersionUID = 1387783792570542451L;

    /**
     * 管理员id(aq_permission_user表的id)
     * {@link com.aq.facade.entity.permission.User}
     */
    @Column(name = "userId")
    private Integer userId;
    /**
     * 客户经理id(aq_manager表id)
     * {@link com.aq.facade.entity.manager.Manager}
     */
    @Column(name = "managerId")
    private Integer managerId;
}
