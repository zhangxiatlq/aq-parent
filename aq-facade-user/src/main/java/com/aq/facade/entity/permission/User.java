package com.aq.facade.entity.permission;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：后台管理员aq_permission_user表的实体
 * @作者： 张霞
 * @创建时间： 16:53 2018/1/19
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "aq_permission_user")
public class User extends BaseEntity{

    private static final long serialVersionUID = -317750977702926237L;


    /**
     * 姓名
     */
    @Column(name = "userName")
    private String userName;
    /**
     *工号（登录账号）
     */
    @Column(name = "employeeID")
    private String employeeID;
    /**
     * 联系手机号
     */
    @Column(name = "telphone")
    private String telphone;
    /**
     * 密码（md加密）
     */
    @Column(name = "password")
    private String password;
    /**
     * 是否可用：0不可用(冻结)   1 可用(未冻结)
     */
    @Column(name = "isable")
    private Byte isable;
    /**
     * 登录时间
     */
    @Column(name = "loginTime")
    private Date loginTime;

    /**
     * 是否删除：1=是，2-否
     */
    @Column(name = "isDelete")
    private Byte isDelete;

    /**
     * 分润比例（包含：vip购买），默认0
     */
    @Column(name = "divideScale")
    private Double divideScale;
}
