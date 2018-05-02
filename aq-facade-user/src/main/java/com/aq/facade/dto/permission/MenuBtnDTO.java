package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 菜单按钮关系DTO
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class MenuBtnDTO implements Serializable {

    private static final long serialVersionUID = -5732747489277713919L;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 按钮id
     */
    private List<Integer> btnIds;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 创建人
     */
    private Integer createrId;

    /**
     * 创建时间
     */
    private Date createTime;
}
