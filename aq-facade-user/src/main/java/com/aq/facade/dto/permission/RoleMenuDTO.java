package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单DTO
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class RoleMenuDTO implements Serializable {

    private static final long serialVersionUID = -5732747489277713919L;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private List<Integer> menuIds;
    /**
     * 创建人
     */
    private Integer createrId;

    /**
     * 角色code
     */
    private Byte roleCode;

}
