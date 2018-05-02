package com.aq.facade.entity.permission;


import com.aq.core.base.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @项目：vul-facade-permission
 * @描述： 菜单实体表
 * @作者： 张霞
 * @创建时间：2018-01-19
 * @Copyright @2018 by zhangxia
 */
@Table(name = "aq_permission_menu")
@Data
public class Menu implements Serializable{

    private static final long serialVersionUID = 3409139139798172232L;

    @Id
    private Integer id;

    /**
     * 菜单名称
     */
    @Column(name = "menuName")
    private String menuName;

    /**
     * 菜单地址
     */
    @Column(name = "menuUrl")
    private String menuUrl;

    /**
     * 菜单父id
     */
    @Column(name = "parentId")
    private Integer parentId;

    /**
     * 菜单顺序
     */
    @Column(name = "sequence")
    private Integer sequence;

    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 特殊菜单 0-基础菜单 1-服务菜单
     */
    @Column(name = "type")
    private Byte type;

}
