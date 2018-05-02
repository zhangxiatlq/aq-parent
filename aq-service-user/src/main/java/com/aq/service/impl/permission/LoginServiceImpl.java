package com.aq.service.impl.permission;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.permission.UserDTO;
import com.aq.facade.entity.permission.User;
import com.aq.facade.service.permission.ILoginService;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.facade.vo.permission.UserVO;
import com.aq.mapper.permission.UserMapper;
import com.aq.mapper.permission.UserRoleMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * LoginServiceImpl
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Slf4j
@Service(version = "1.0.0")
public class LoginServiceImpl implements ILoginService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;


    @Override
    public Result login(UserDTO userDTO) {
        log.info("登录参数入参，user={}", JSON.toJSONString(userDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            UserDTO query = new UserDTO();
            query.setEmployeeID(userDTO.getEmployeeID());
            query.setRoleCode(userDTO.getRoleCode());
            List<UserVO> list = userMapper.getUserByCondition(userDTO);
            if (CollectionUtils.isEmpty(list)) {
                return result;
            }
            UserVO dbUser = list.get(0);
            //更新登录日期
            User loginUser = new User();
            loginUser.setId(dbUser.getId());
            loginUser.setLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(loginUser);
            //存用户Session
            SessionUserVO sessionUserVo = new SessionUserVO();
            sessionUserVo.setId(dbUser.getId());
            sessionUserVo.setUserName(dbUser.getUserName());
            sessionUserVo.setEmployeeID(dbUser.getEmployeeID());
            sessionUserVo.setTelphone(dbUser.getTelphone());
            sessionUserVo.setSessionRoleVo(userRoleMapper.selectRoleVOByUserId(dbUser.getId(), query.getRoleCode()));
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, sessionUserVo);
        } catch (Exception ex) {
            log.error("登录异常, ex={}", ex);
        }
        log.info("登录参数返回值，result={}", JSON.toJSONString(result));
        return result;
    }
}
