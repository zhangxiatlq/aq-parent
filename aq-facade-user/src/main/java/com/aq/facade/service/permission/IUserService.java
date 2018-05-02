package com.aq.facade.service.permission;


import com.aq.facade.dto.permission.*;
import com.aq.facade.entity.permission.User;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 用户接口
 *
 * @author 郑朋
 * @create 2018/1/20
 */
public interface IUserService {

    /**
     * 根据条件查询用户
     *
     * @param userDto
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getUserByCondition(UserDTO userDto);

    /**
     * 添加管理员
     *
     * @param addUserDTO
     * @author: 张霞
     * @createDate: 17:47 2018/1/19
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result addUser(AddUserDTO addUserDTO);


    /**
     * 查询管理员列表
     *
     * @param pageBean
     * @param selectUserDTO
     * @author: 张霞
     * @createDate: 13:07 2018/1/20
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result getUserListByPage(PageBean pageBean, SelectUserDTO selectUserDTO);


    /**
     * 修改用户信息
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updateUser(User user);


    /**
     * 冻结或者启用用户
     *
     * @param userStartOrFrozenDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result frozenOrEnableUser(UserStartOrFrozenDTO userStartOrFrozenDTO);


    /**
     * 查询用户
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getUserById(User user);


    /**
     * 修改用户对应的角色
     *
     * @param updateUserRoleDto
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updateUserRole(UpdateUserRoleDTO updateUserRoleDto);


    /**
     * 重置初始化用户密码
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result resetPassword(User user);


    /**
     * 修改密码(后台首页)
     *
     * @param updatePassDto
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updatePass(UpdatePassDTO updatePassDto);


    /**
     * 通过用户id查询用户所拥有的角色id
     *
     * @param userId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getUserRoleByUserId(Integer userId);

    /**
     * 更据条件查询管理员
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    Result getUserByList(User user);

    /**
     * 设置vip 分成
     *
     * @param setVIPDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/21
     */
    Result setDivideScale(SetVIPDTO setVIPDTO);

    /**
     * 获取 用户的安全设置
     *
     * @param userId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/21
     */
    Result getSecurityByUserId(Integer userId);

    /**
     * 修改 用户的安全设置
     *
     * @param updateUserSecurityDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result updateSecurity(UpdateUserSecurityDTO updateUserSecurityDTO);
}
