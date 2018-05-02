package com.aq.facade.dto.permission;


import com.aq.facade.vo.permission.MenuTreeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 获取菜单按钮列表
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class GetMenuTreeDTO implements Serializable {

    private static final long serialVersionUID = -5350629229232288288L;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 获取菜单树列表
     */
    private List<MenuTreeVO> menuTreeVOS;
}
