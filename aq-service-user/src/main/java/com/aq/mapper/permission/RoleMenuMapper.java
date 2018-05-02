package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.permission.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleMenuMapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 通过角色id删除角色菜单
     *
     * @param roleId
     * @return int
     * @author 郑朋
     * @create 2017/5/17
     */
    int deleteRoleMenuByRoleId(@Param("roleId") Integer roleId);


    /**
     * 给角色id赋予菜单
     *
     * @param roleId
     * @param menuIds
     * @return int
     * @author 郑朋
     * @create 2017/5/17
     */
    int insertRoleMenu(@Param("roleId") Integer roleId, @Param("menuIds") List<Integer> menuIds);


    /**
     * 通过角色id查询角色菜单
     *
     * @param roleId
     * @return java.util.List<java.lang.Long>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<Integer> selectRoleMenuByRoleId(@Param("roleId") Integer roleId);

}
