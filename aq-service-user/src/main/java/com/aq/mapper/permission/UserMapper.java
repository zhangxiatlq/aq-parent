package com.aq.mapper.permission;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.permission.SelectUserDTO;
import com.aq.facade.dto.permission.UserDTO;
import com.aq.facade.entity.permission.User;
import com.aq.facade.vo.permission.UserListVO;
import com.aq.facade.vo.permission.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * user-mapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页获取管理员列表
     *
     * @param selectUserDTO
     * @author: 张霞
     * @createDate: 13:22 2018/1/20
     * @return: java.util.List<com.aq.facade.vo.permission.UserListVO>
     * @version: 2.1
     */
    List<UserListVO> selectUserListByPage(SelectUserDTO selectUserDTO);

    /**
     * 根据条件查询用户
     *
     * @param userDto
     * @return 用户对象
     * @author 郑朋
     * @createTime 2018/1/20
     */
    List<UserVO> getUserByCondition(UserDTO userDto);
}
