package com.aq.facade.entity.permission;


import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @项目：aq-facade-user
 * @描述： 菜单按钮实体
 * @作者： 张霞
 * @创建时间：2018-01-19
 * @Copyright @2018 by zhangxia
 */
@Table(name = "aq_permission_menu_btn")
@Data
public class MenuBtn implements Serializable{

    private static final long serialVersionUID = 8355710622359264368L;

    @Id
    private Integer id;

    /**
     * 菜单id
     */
    @Column(name = "menuId")
    private Integer menuId;


    /**
     * 角色id
     */
    @Column(name = "roleId")
    private Integer roleId;

    /**
     * 按钮id
     */
    @Column(name = "btnId")
    private Integer btnId;
}
