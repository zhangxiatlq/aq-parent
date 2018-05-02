package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.permission.UserRole;
import com.aq.facade.vo.permission.SessionRoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRoleMapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id删除用户角色关系
     *
     * @param userId
     * @return int
     * @author 郑朋
     * @create 2017/5/17
     */
    int deleteUserRoleByUserId(@Param("userId") Integer userId);


    /**
     * 通过用户id查询用户对应的角色id
     *
     * @param userId
     * @return java.util.List<java.lang.Integer>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<Integer> selectUserRoleByUserId(@Param("userId") Integer userId);


    /**
     * 给用户赋予角色
     *
     * @param userId
     * @param roleIds
     * @return int
     * @methodname insertUserRole 的描述：
     * @author 郑朋
     * @create 2017/5/17
     */
    int insertUserRole(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

    /**
     * 通过用户id查询role
     *
     * @param userId
     * @param roleCode
     * @return java.util.List<com.aq.facade.vo.permission.SessionRoleVO>
     * @author 郑朋
     * @create 2017/5/17
     */
    List<SessionRoleVO> selectRoleVOByUserId(@Param("userId") Integer userId, @Param("roleCode") Byte roleCode);

    /**
     * 更据用户id和角色类型查询
     *
     * @param userId
     * @param roleCode
     * @return java.util.List<com.aq.facade.vo.permission.SessionRoleVO>
     * @author 郑朋
     * @create 2017/10/23
     */
    List<SessionRoleVO> selectRoleByUserId(@Param("userId") Integer userId, @Param("roleCode") Byte roleCode);

    /**
     * 新增用户对应的角色
     *
     * @param userRole
     * @return int
     * @author 郑朋
     * @create 2017/4/24
     */
    int insertUserRoleOne(UserRole userRole);


    /**
     * 查询角色用户
     *
     * @param userRole
     * @return java.util.List<com.aq.facade.entity.permission.UserRole>
     * @author 郑朋
     * @create 2017/4/24
     */
    List<UserRole> selectUserRole(UserRole userRole);

}
