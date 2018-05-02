package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：
 * @作者： 张霞
 * @创建时间： 16:19 2018/1/18
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "usermanage_userinfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 8504164487312697106L;
    @Id
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "phonenum")
    private String phonenum;
    /**
     *注：后台添加客户经理的时候，直接填充为12
     */
    @Column(name = "role")
    private Integer role;
    /**
     * AuthUser表的id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 后台添加可以为空
     */
    @Column(name = "openid")
    private String openid;
    /**
     * 默认0-客户，1-客户经理
     */
    @Column(name = "rolename")
    private Integer rolename;
    /**
     * 工号
     */
    @Column(name = "staffnum")
    private Integer staffnum;
    /**
     * 资产
     */
    @Column(name = "fortunellavenosa")
    private Integer fortunellavenosa;

    /**
     *备注
     */
    @Column(name = "remark")
    private String remark;
}
