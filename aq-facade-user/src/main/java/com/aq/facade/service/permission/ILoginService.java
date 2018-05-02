package com.aq.facade.service.permission;


import com.aq.facade.dto.permission.UserDTO;
import com.aq.util.result.Result;


/**
 * 登录Service
 *
 * @author 郑朋
 * @create 2018/1/20
 */
public interface ILoginService {


    /**
     * 登录校验
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result login(UserDTO user);
}
