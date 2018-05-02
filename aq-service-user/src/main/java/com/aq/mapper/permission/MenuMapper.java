package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.permission.Menu;
import com.aq.facade.vo.permission.MenuVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * user-mapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色id获取菜单
     *
     * @param roleIds
     * @return java.util.List<com.aq.facade.vo.permission.MenuVO>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<MenuVO> getMenuByRoleIds(@Param("roleIds") List<Integer> roleIds);


    /**
     * 通过角色id查询该角色的基础菜单
     *
     * @param roleId
     * @return java.util.List<com.aq.facade.vo.permission.MenuVO>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<MenuVO> getAllBaseMenuByRoleId(@Param("roleId") Integer roleId);


    /**
     * 查询所有菜单
     *
     * @return java.util.List<com.aq.facade.vo.permission.MenuVO>
     * @author 郑朋
     * @create 2017/4/24
     */
    List<MenuVO> getAllMenu();
}
