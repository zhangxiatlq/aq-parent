package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -6244664223048744656L;

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色Code
     */
    private Byte roleCode;

    /**
     * 0不可用(冻结)   1 可用(未冻结)
     */
    private Byte status;


    /**
     * 创建人
     */
    private Integer createrId;
}
