package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.permission.MenuBtn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MenuBtnMapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface MenuBtnMapper extends BaseMapper<MenuBtn> {

    /**
     * 通过角色id和菜单id删除菜单按钮
     *
     * @param menuIds
     * @param roleId
     * @return int
     * @author 郑朋
     * @create 2017/5/13
     */
    int deleteBtnByMenuIdAndRoleId(@Param("menuIds") List<Integer> menuIds, @Param("roleId") Integer roleId);

    /**
     * 给菜单id赋予按钮
     *
     * @param menuId
     * @param btnIds
     * @param roleId
     * @return int
     * @author 郑朋
     * @create 2017/5/13
     */
    int insertMenuBtn(@Param("menuId") Integer menuId, @Param("btnIds") List<Integer> btnIds,
                      @Param("roleId") Integer roleId);

    /**
     * 通过菜单id和角色Id查询菜单按钮
     *
     * @param menuId
     * @param roleId
     * @return java.util.List<com.aq.facade.entity.permission.MenuBtn>
     * @author 郑朋
     * @create 2017/5/13
     */
    List<MenuBtn> selectMenuBtnByMenuIdAndRoleId(@Param("menuId") Integer menuId, @Param("roleId") Integer roleId);

}
