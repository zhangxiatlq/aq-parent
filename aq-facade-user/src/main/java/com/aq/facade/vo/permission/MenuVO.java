package com.aq.facade.vo.permission;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单vo
 *
 * @author 郑朋
 * @create 2018/1/19
 **/

@Data
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 2267431338902973275L;


    /**
     * id
     */
    private Integer id;

    /**
     * 按钮专用Id
     */
    private Integer btnId;

    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单地址
     */
    private String menuUrl;
    /**
     * 菜单父id
     */
    private Integer parentId;

    /**
     * 是否选择 1：选择 2：未选择
     */
    private Integer isChecked;

    /**
     * 顺序
     */
    private Integer sequence;

    /**
     * 子菜单
     */
    private List<MenuVO> child;

    /**
     * 菜单图标
     */
    private String icon;

    private Byte type;

}
