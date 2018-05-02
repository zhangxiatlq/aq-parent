package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.permission.RoleDTO;
import com.aq.facade.entity.permission.Role;
import com.aq.facade.vo.permission.RoleVO;
import com.aq.facade.vo.permission.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleMapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId
     * @return java.util.List<com.aq.facade.vo.permission.RoleVO>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<RoleVO> getRoleListByUserId(@Param("userId") Integer userId);

    /**
     * 分页查询角色信息
     *
     * @param roleDTO
     * @return java.util.List<com.aq.facade.vo.permission.RoleVO>
     * @author 郑朋
     * @create 2017/4/24
     */
    List<RoleVO> getRoleByPage(RoleDTO roleDTO);

    /**
     * 查询所有的角色
     *
     * @param
     * @return java.util.List<com.aq.facade.vo.permission.RoleVO>
     * @author 郑朋
     * @create 2017/4/24
     */
    List<RoleVO> getAllRole();

    /**
     * 通过条件查询角色
     *
     * @param role
     * @return java.util.List<com.aq.facade.entity.permission.Role>
     * @author 郑朋
     * @create 2017/4/24
     */
    List<Role> selectRoleBySelective(Role role);

    /**
     * 通过角色Id查询用户列表
     *
     * @param roleId
     * @return List<com.aq.facade.vo.permission.UserVO>
     * @author 郑朋
     * @create 2017/5/13
     */
    List<UserVO> getUserListByRoleId(@Param("roleId") Integer roleId);
}
