package com.aq.service.impl.permission;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.permission.*;
import com.aq.facade.entity.manager.Balance;
import com.aq.facade.entity.permission.SecurityBaseSetting;
import com.aq.facade.entity.permission.User;
import com.aq.facade.entity.permission.UserSecurity;
import com.aq.facade.enums.permission.PermissionConstants;
import com.aq.facade.enums.permission.PermissionEnum;
import com.aq.facade.exception.permission.PermissionBizException;
import com.aq.facade.exception.permission.PermissionExceptionEnum;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.permission.UserListVO;
import com.aq.facade.vo.permission.UserSecurityVO;
import com.aq.mapper.manager.BalanceMapper;
import com.aq.mapper.permission.SecurityBaseSettingMapper;
import com.aq.mapper.permission.UserMapper;
import com.aq.mapper.permission.UserRoleMapper;
import com.aq.mapper.permission.UserSecurityMapper;
import com.aq.util.encrypt.MD5;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * UserServiceImpl
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Slf4j
@Service(version = "1.0.0")
public class UserServiceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    private UserSecurityMapper userSecurityMapper;

    @Autowired
    private SecurityBaseSettingMapper securityBaseSettingMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Override
    public Result getUserByCondition(UserDTO userDto) {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        log.info("根据条件查询管理员信息入参, userDto={}", JSON.toJSONString(userDto));
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, userMapper.getUserByCondition(userDto));
        } catch (Exception e) {
            log.error("根据条件查询管理员信息异常, e={}", e);
        }
        log.info("根据条件查询管理员信息返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addUser(AddUserDTO addUserDTO) {
        try {
            log.info("新增管理员入参，addUserDTO={}", JSON.toJSONString(addUserDTO));
            Result result = ResultUtil.getResult(RespCode.Code.FAIL);
            //验证表单参数
            String message = addUserDTO.validateForm();
            if (StringUtils.isNotEmpty(message)) {
                result.setMessage(message);
                return result;
            }

            //校验新增用户
            User user = new User();
            user.setEmployeeID(addUserDTO.getEmployeeID());
            List<User> list = userMapper.select(user);
            if (CollectionUtils.isNotEmpty(list)) {
                result.setMessage(message);
                return ResultUtil.getResult(PermissionExceptionEnum.REGISTERED);
            }
            user = new User();
            BeanUtils.copyProperties(addUserDTO, user);
            user.setIsable(PermissionEnum.YES_USE.getCode());
            user.setCreateTime(new Date());
            user.setIsDelete(PermissionEnum.NO_DELETE.getCode());
            user.setDivideScale(0.0);
            //新增用戶
            userMapper.insertUseGeneratedKeys(user);
            //初始化余额
            BigDecimal initMoney = new BigDecimal(0);
            Balance balance = new Balance();
            balance.setManagerId(user.getId());
            balance.setMoney(initMoney);
            balance.setCreaterId(user.getId());
            balance.setCreateTime(new Date());
            balance.setRoleType(RoleCodeEnum.ADMIN.getCode());
            balance.setTotalRevenue(initMoney);
            balance.setTotalSettlement(initMoney);
            balanceMapper.insert(balance);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增管理员返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception ex) {
            log.error("新增管理员异常，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.ADD_USER_ERROR);

        }
    }

    @Override
    public Result getUserListByPage(PageBean pageBean, SelectUserDTO selectUserDTO) {
        log.info("分页查询管理员入参, pageBean={},selectUserDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(selectUserDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<UserListVO> list = userMapper.selectUserListByPage(selectUserDTO);
            PageInfo<UserListVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception ex) {
            log.error("分页查询管理员异常，ex={}", ex);
        }
        log.info("分页查询管理员返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateUser(User user) {
        try {
            log.info("修改管理员信息入参,user={}", JSON.toJSONString(user));
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception ex) {
            log.error("修改管理员信息异常，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_USER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result frozenOrEnableUser(UserStartOrFrozenDTO userStartOrFrozenDTO) {
        try {
            log.info("冻结或者启用管理员入参,userStartOrFrozenDTO={}", JSON.toJSONString(userStartOrFrozenDTO));
            Result result = ResultUtil.getResult(RespCode.Code.FAIL);
            //验证表单参数
            String message = userStartOrFrozenDTO.validateForm();
            if (StringUtils.isNotEmpty(message)) {
                result.setMessage(message);
                return result;
            }
            //数据操作
            User user = new User();
            BeanUtils.copyProperties(userStartOrFrozenDTO, user);
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return ResultUtil.getResult(RespCode.Code.SUCCESS, userMapper.selectByPrimaryKey(user.getId()));
        } catch (Exception e) {
            log.error("冻结管理员异常，ex={}", e);
            throw new PermissionBizException(PermissionExceptionEnum.DISABLE_ERROR);
        }
    }

    @Override
    public Result getUserById(User user) {
        log.info("查询管理员入参, user={}", JSON.toJSONString(user));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, userMapper.select(user));
        } catch (Exception ex) {
            log.error("查询管理员异常，ex={}", ex);
        }
        log.info("查询管理员返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateUserRole(UpdateUserRoleDTO updateUserRoleDto) {
        try {
            log.info("修改管理员对应的角色入参，updateUserRoleDto={}", JSON.toJSONString(updateUserRoleDto));
            userRoleMapper.deleteUserRoleByUserId(updateUserRoleDto.getUserId());
            userRoleMapper.insertUserRole(updateUserRoleDto.getUserId(), updateUserRoleDto.getRoleIds());
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改管理员对应的角色异常，ex={}", e);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_USER_ROLE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result resetPassword(User user) {
        try {
            log.info("重置密码入参，user={}", JSON.toJSONString(user));
            if (user.getId() == null) {
                return ResultUtil.getResult(RespCode.Code.FAIL, "主键Id不能为空");
            }
            user.setUpdateTime(new Date());
            user.setPassword(MD5.getMD5Str(PermissionConstants.PASSWORD));
            userMapper.updateByPrimaryKeySelective(user);
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("重置密码异常", e);
            throw new PermissionBizException(PermissionExceptionEnum.RESET_PASSWORD_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updatePass(UpdatePassDTO updatePassDTO) {
        try {
            Result result;
            log.info("(后台)修改密码入参,updatePassDto={}", JSON.toJSONString(updatePassDTO));
            User user = userMapper.selectByPrimaryKey(updatePassDTO.getId());
            String oldPass = MD5.getMD5Str(updatePassDTO.getPassword());
            String newPass = MD5.getMD5Str(updatePassDTO.getNewPassword());
            if (user != null && StringUtils.isNotBlank(user.getPassword())
                    && user.getPassword().equals(oldPass)) {
                User mdyUser = new User();
                mdyUser.setId(updatePassDTO.getId());
                mdyUser.setPassword(newPass);
                mdyUser.setUpdaterId(updatePassDTO.getId());
                mdyUser.setUpdateTime(new Date());
                userMapper.updateByPrimaryKeySelective(mdyUser);
                result = ResultUtil.getResult(PermissionExceptionEnum.Code.SUCCESS);
            } else {
                result = ResultUtil.getResult(PermissionExceptionEnum.PASSWORD_ERROR);
            }
            log.info("(后台)修改密码返回值,result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception ex) {
            log.error("(后台)修改密码入参，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_PASSWORD_ERROR);
        }
    }

    @Override
    public Result getUserRoleByUserId(Integer userId) {
        log.info("通过管理员id查询管理员所拥有的角色id入参, userId={}", userId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, userRoleMapper.selectUserRoleByUserId(userId));
        } catch (Exception ex) {
            log.error("通过管理员id查询管理员所拥有的角色id异常，ex={}", ex);
        }
        log.info("通过管理员id查询管理员所拥有的角色id返回值, result={}", JSON.toJSONString(result));
        return result;
    }


    @Override
    public Result getUserByList(User user) {
        log.info("更据条件查询管理员入参, user={}", JSON.toJSONString(user));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, userMapper.select(user));
        } catch (Exception ex) {
            log.error("更据条件查询管理员异常，ex={}", ex);
        }
        log.info("更据条件查询管理员返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result setDivideScale(SetVIPDTO setVIPDTO) {
        log.info("设置vip 分成入参, setVIPDTO={}", JSON.toJSONString(setVIPDTO));
        try {
            User user = new User();
            BeanUtils.copyProperties(setVIPDTO, user);
            userMapper.updateByPrimaryKeySelective(user);
            Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("设置vip 分成返回值, result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception ex) {
            log.error("设置vip 分成异常，ex={}", ex);
            throw new PermissionBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public Result getSecurityByUserId(Integer userId) {
        log.info("获取 用户的安全设置入参, userId={}", userId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (null == userId) {
                return ResultUtil.setResult(false, "员工id不能为空");
            }
            UserSecurity userSecurity = new UserSecurity();
            userSecurity.setUserId(userId);
            List<SecurityBaseSetting> baseSettings = securityBaseSettingMapper.selectAll();
            List<UserSecurity> list = userSecurityMapper.select(userSecurity);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, getUserSecurityVO(baseSettings, list));
        } catch (Exception ex) {
            log.error("获取 用户的安全设置异常，ex={}", ex);
        }
        log.info("获取 用户的安全设置返回值, result={}", JSON.toJSONString(result));
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateSecurity(UpdateUserSecurityDTO updateUserSecurityDTO) {
        try {
            log.info("修改 用户的安全设置入参，updateUserSecurityDTO={}", JSON.toJSONString(updateUserSecurityDTO));
            UserSecurity userSecurity = new UserSecurity();
            userSecurity.setUserId(updateUserSecurityDTO.getUserId());
            userSecurityMapper.delete(userSecurity);
            if (CollectionUtils.isNotEmpty(updateUserSecurityDTO.getIds())) {
                List<UserSecurity> list = new ArrayList<>();
                for (Integer id : updateUserSecurityDTO.getIds()) {
                    userSecurity = new UserSecurity();
                    userSecurity.setUserId(updateUserSecurityDTO.getUserId());
                    userSecurity.setSecurityId(id);
                    list.add(userSecurity);
                }
                userSecurityMapper.insertList(list);
            }
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改 用户的安全设置异常，ex={}", e);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_USER_ROLE_ERROR);
        }
    }

    private List<UserSecurityVO> getUserSecurityVO(List<SecurityBaseSetting> baseSettings, List<UserSecurity> list) {
        if (CollectionUtils.isNotEmpty(baseSettings)) {
            UserSecurityVO userSecurityVO;
            List<UserSecurityVO> userSecurityVOS = new ArrayList<>();
            Map<Integer, Integer> map = new HashMap<>(16);
            if (CollectionUtils.isNotEmpty(list)) {
                for (UserSecurity userSecurity : list) {
                    map.put(userSecurity.getSecurityId(), userSecurity.getUserId());
                }
            }
            for (SecurityBaseSetting securityBaseSetting : baseSettings) {
                userSecurityVO = new UserSecurityVO();
                BeanUtils.copyProperties(securityBaseSetting, userSecurityVO);
                userSecurityVO.setChecked(map.containsKey(securityBaseSetting.getId()) ? 1 : 2);
                userSecurityVOS.add(userSecurityVO);
            }
            return userSecurityVOS;
        }
        return new ArrayList<>();
    }
}
