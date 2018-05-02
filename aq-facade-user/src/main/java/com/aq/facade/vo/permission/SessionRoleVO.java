package com.aq.facade.vo.permission;


import lombok.Data;

import java.io.Serializable;

/**
 * 登陆角色vo实体
 *
 * @author zhengpeng
 */
@Data
public class SessionRoleVO implements Serializable {

    private static final long serialVersionUID = 1271305496020041687L;

    /**
     * 角色id
     */
    private Integer id;


    private String roleName;

    /**
     * 角色code{@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte roleCode;


}
