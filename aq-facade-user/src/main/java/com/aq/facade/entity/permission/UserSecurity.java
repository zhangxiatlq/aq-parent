package com.aq.facade.entity.permission;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 员工安全设置
 *
 * @author 郑朋
 * @create 2018/3/21
 */
@Table(name = "aq_user_security")
@Data
public class UserSecurity implements Serializable {


    private static final long serialVersionUID = 869270823614662378L;
    @Id
    private Integer id;

    /**
     * 员工id
     */
    @Column(name = "userId")
    private Integer userId;

    /**
     * 安全设置基础项id
     */
    @Column(name = "securityId")
    private Integer securityId;

}
