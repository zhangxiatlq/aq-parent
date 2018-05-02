package com.aq.facade.service.permission;


import com.aq.facade.dto.permission.RoleDTO;
import com.aq.facade.dto.permission.RoleMenuDTO;
import com.aq.facade.entity.permission.Role;
import com.aq.facade.vo.permission.RoleVO;
import com.aq.facade.vo.permission.UserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

import java.util.List;
import java.util.Map;

/**
 * 角色接口
 *
 * @author 郑朋
 * @create 2018/1/20
 */
public interface IRoleService {


    /**
     * 新增角色
     *
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result addRole(Role role);


    /**
     * 修改角色
     *
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updateRole(Role role);


    /**
     * 通过角色id查询角色信息
     *
     * @param roleId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getRoleById(Integer roleId);


    /**
     * 分页查询角色
     *
     * @param page
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getRoleListByPage(PageBean page, RoleDTO role);


    /**
     * 根据条件查询所有角色
     *
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getRoleListByCondition(RoleDTO role);


    /**
     * 通过角色id查询菜单
     *
     * @param roleDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getRoleMenuAndMenuBtnByRoleId(RoleDTO roleDTO);


    /**
     * 通过角色修改角色菜单和菜单按钮
     *
     * @param roleMenuDto
     * @param menuButtons
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updateRoleMenuAndMenuBtn(RoleMenuDTO roleMenuDto, Map<Integer, List<Integer>> menuButtons);


    /**
     * 通过角色Id查询用户列表
     *
     * @param roleId
     * @return java.util.List<com.aq.facade.vo.permission.UserVO>
     * @author 郑朋
     * @create 2018/1/20
     */
    List<UserVO> getUsersByRoleId(Integer roleId);


    /**
     * 根据用户Id查询角色集合
     *
     * @param userId
     * @return java.util.List<com.aq.facade.vo.permission.RoleVO>
     * @author 郑朋
     * @create 2018/1/20
     */

    List<RoleVO> getRoleByUserId(Integer userId);
}
