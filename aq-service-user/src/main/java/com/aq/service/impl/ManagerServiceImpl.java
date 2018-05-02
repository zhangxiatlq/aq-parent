package com.aq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.*;
import com.aq.facade.dto.AddManagerDTO;
import com.aq.facade.dto.UpdateManagerDTO;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.entity.AuthUser;
import com.aq.facade.entity.UserInfo;
import com.aq.facade.entity.UserManager;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.exception.permission.UserExceptionEnum;
import com.aq.facade.service.IManagerService;
import com.aq.facade.vo.ManagerDetailVO;
import com.aq.facade.vo.manage.ManagerListVO;
import com.aq.mapper.AuthUserMapper;
import com.aq.mapper.UserInfoMapper;
import com.aq.mapper.UserManagerMapper;
import com.aq.util.encrypt.SHA256Python;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户经理service接口实现
 * @作者： 张霞
 * @创建时间： 21:20 2018/1/21
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Service(version = "1.0.0")
public class ManagerServiceImpl implements IManagerService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserManagerMapper userManagerMapper;

    /**
     * @param pagebean
     * @param selectManagerDTO
     * @author: 张霞
     * @description：翻页获取客户经理列表
     * @createDate: 21:19 2018/1/21
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getManagerListByPage(PageBean pagebean, SelectManagerDTO selectManagerDTO) {

        log.info("客户经理后台列表查询入参参数pageBean = {},selectManagerDTO = {}", JSON.toJSONString(pagebean), JSON.toJSONString(selectManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pagebean != null) {
                pagebean.setPageSize(pagebean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pagebean.getPageSize());
                pagebean.setPageNum(pagebean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pagebean.getPageNum());
                PageHelper.startPage(pagebean.getPageNum(), pagebean.getPageSize());
            }
            List<ManagerListVO> managerListVOS = authUserMapper.getManagerListByPage(selectManagerDTO);
            if (!RoleCodeEnum.ADMIN.getCode().equals(selectManagerDTO.getRoleCode())){
                for (ManagerListVO managerListVO:
                     managerListVOS) {
                    managerListVO.setTelphone(StringTools.telphoneChange(managerListVO.getTelphone()));
                }
            }
            PageInfo<ManagerListVO> pageInfo = new PageInfo<>(managerListVOS);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());

        } catch (Exception e) {
            log.info("查询异常e={}", e);
            result.setMessage("客户经理查询异常");
        }
        log.info("获取客户经理列表处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @param addManagerDTO
     * @author: 张霞
     * @description：后台添加客户经理
     * @createDate: 21:44 2018/1/21
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Transactional
    @Override
    public Result addManager(AddManagerDTO addManagerDTO) {
        log.info("后台添加客户经理入参参数addManagerDTO={}", JSON.toJSONString(addManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = addManagerDTO.validateForm();
            if (StringUtils.isEmpty(message)) {
				AuthUser selectOne = new AuthUser();
				selectOne.setUsername(addManagerDTO.getUsername());
				selectOne = authUserMapper.selectOne(selectOne);
				if (null != selectOne) {
					result.setMessage("用户已存在不能重复添加");
					return result;
				}
                //添加authUser表
                AuthUser authUser = new AuthUser();
                authUser.setDateJoined(new Date());
                authUser.setUsername(addManagerDTO.getUsername());
                authUser.setIsActive(IsActiveEnum.IS_NOT_ACTIVE_ENUM.getCode());
                authUser.setIsStaff(IsStaffEnum.IS_NOT_STAFF_ENUM.getCode());
                String telphone = addManagerDTO.getTelphone();
                authUser.setPassword(SHA256Python.encode(addManagerDTO.getTelphone().substring(telphone.length()-4,telphone.length())));//默认手机手机后4位
                authUser.setIsSuperuser(IsSuperuserEnum.IS_NOT_SUPERUSER_ENUM.getCode());
                if (authUserMapper.insertUseGeneratedKeys(authUser) > 0) {
                    //插入auth_user表成功后再进入下一步
                    log.info("插入auth_user表数据成功，插入数据id={}", authUser.getId());
                    UserInfo userInfo = new UserInfo();
                    userInfo.setFortunellavenosa(addManagerDTO.getFortunellavenosa());
                    userInfo.setUserId(authUser.getId());
                    userInfo.setName(addManagerDTO.getName());
                    userInfo.setPhonenum(addManagerDTO.getTelphone());
                    userInfo.setRole(12);//客户经理直接填写12
                    userInfo.setRolename(UserTypeEnum.CUSTOMER_MANAGER.getCode().intValue());
                    userInfo.setStaffnum(addManagerDTO.getStaffnum());
                    userInfo.setRemark(addManagerDTO.getRemark());
                    if (userInfoMapper.insertUseGeneratedKeys(userInfo) > 0) {
                        //插入usermanage_userinfo表成功后，再进行aq_user_manager表的插入
                        log.info("插入usermanager_userinfo表成功，获取插入数据id={}", userInfo.getId());
                        UserManager userManager = new UserManager();
                        userManager.setManagerId(authUser.getId());
                        userManager.setUserId(addManagerDTO.getEmployId());
                        userManager.setCreaterId(addManagerDTO.getCreaterId());
                        userManager.setCreateTime(new Date());
                        if (userManagerMapper.insertUseGeneratedKeys(userManager) > 0) {
                            log.info("插入aq_user_manager表成功");
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        } else {
                            log.info("插入aq_user_manager表失败");
                            throw new UserBizException(UserExceptionEnum.ADD_MANAGER_FAIL);
                        }

                    } else {
                        log.info("插入usermanager_userinfo表失败");
                        throw new UserBizException(UserExceptionEnum.ADD_MANAGER_FAIL);
                    }

                } else {
                    log.info("插入auth_user表失败");
                }
            } else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("添加客户经理异常e={}，message={}", e, e.getMessage());
            throw new UserBizException(UserExceptionEnum.ADD_MAAGER_EXCEPTION);
        }
        log.info("后台添加客户经理处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @param selectManagerDTO
     * @author: 张霞
     * @description：后台获取客户经理的详细信息
     * @createDate: 23:19 2018/1/21
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getManagerDetail(SelectManagerDTO selectManagerDTO) {
        log.info("后台获取客户经理的详细信息入参参数selectManagerDTO={}", JSON.toJSONString(selectManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (Objects.isNull(selectManagerDTO.getId())||selectManagerDTO.getId().intValue()==0){
                result.setMessage("参数不能为空");
            }else {
                ManagerDetailVO managerDetail = authUserMapper.getManagerDetail(selectManagerDTO);
                if (!RoleCodeEnum.ADMIN.getCode().equals(selectManagerDTO.getRoleCode())){
                    managerDetail.setTelphone(StringTools.telphoneChange(managerDetail.getTelphone()));
                }
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, managerDetail);
            }

        } catch (Exception e) {
            log.info("后台获取客户经理的详细异常e={}", e);
        }
        log.info("后台获取客户经理的详细信息处理结果result={}", JSON.toJSONString(selectManagerDTO));
        return result;
    }

    /**
     * @author: 张霞
     * @description：编辑客户经理信息
     * @createDate: 9:53 2018/1/22
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result editManager(UpdateManagerDTO updateManagerDTO) {
        log.info("后台编辑客户经理入参参数updateManagerDTO = {}",JSON.toJSONString(updateManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //更新姓名和资产
            AuthUser authUser = authUserMapper.selectByPrimaryKey(updateManagerDTO.getId());
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(updateManagerDTO.getId());
            userInfo = userInfoMapper.selectOne(userInfo);
            if (Objects.isNull(userInfo)){
                log.info("传入参数有误，没有查到客户经理基本信息原始数据");
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
            }else {
                userInfo.setName(updateManagerDTO.getName());
//                userInfo.setFortunellavenosa(updateManagerDTO.getAccout());
                userInfo.setRemark(updateManagerDTO.getRemark());
                if (userInfoMapper.updateByPrimaryKeySelective(userInfo)>0){
                    //更新userInfo数据完成后，更新与维护人员关联关系表
                    UserManager userManager = new UserManager();
                    userManager.setManagerId(updateManagerDTO.getId());
                    userManager = userManagerMapper.selectOne(userManager);
                    if (Objects.isNull(userManager)){
                        if (Objects.nonNull(updateManagerDTO.getEmployeeId())){
                            //插入信息
                            userManager = new UserManager();
                            userManager.setManagerId(updateManagerDTO.getId());
                            userManager.setUserId(updateManagerDTO.getEmployeeId());
                            userManager.setCreateTime(new Date());
                            userManager.setCreaterId(updateManagerDTO.getUpdaterId());
                            if (userManagerMapper.insert(userManager)>0){
                                //添加成功
                                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                            }else {
                                log.info("添加客户经理和维护人员关联关系失败");
                                throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_ADD_RELATION_FAIL);
                            }
                        }else {
                            //添加成功
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        }

                    }else {
                        userManager.setUserId(updateManagerDTO.getEmployeeId());
                        if (userManagerMapper.updateByPrimaryKeySelective(userManager)>0){
                            //更新成功
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        }else {
                            log.info("更新客户经理和维护人员关联关系失败");
                            throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_UPDATE_RELATION_FAIL);
                        }
                    }
                }else {
                    log.info("更新客户经理基本信息失败");
                }
            }
        }catch (Exception e){
            log.info("添加客户经理异常e={}，message={}", e, e.getMessage());
            throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_EXCEPTION);
        }
        log.info("后台编辑客户经理入参参数updateManagerDTO = {}",JSON.toJSONString(updateManagerDTO));
        return result;
    }


    /**
     * @author: 张霞
     * @description：获取所有客户经理
     * @createDate: 21:18 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getAllManager() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<ManagerListVO> allManager = authUserMapper.getAllManager();
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,allManager);
        }catch (Exception e){
            log.info("获取所有客户经理异常e={}",e);
        }
        log.info("获取所有客户经理处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: 张霞
     * @description：客户经理重置密码
     * @createDate: 17:24 2018/1/23
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result resetPWD(UpdateManagerDTO updateManagerDTO) {
        log.info("客户经理重置密码入参参数updateManagerDTO={}",JSON.toJSONString(updateManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            AuthUser authUser = authUserMapper.selectByPrimaryKey(updateManagerDTO.getId());
            if (Objects.isNull(authUser)){
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
            }else {
                authUser.setPassword(SHA256Python.encode(StringUtils.substring(updateManagerDTO.getTelphone(),updateManagerDTO.getTelphone().length()-6)));
                if (authUserMapper.updateByPrimaryKeySelective(authUser)>0){
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }else {
                    log.info("客户经理重置密码失败");
                    result.setMessage("客户经理重置密码失败");
                }
            }

        }catch (Exception e){
            log.info("客户经理重置密码异常e={}",e);
        }
        log.info("客户经理重置密码处理结果result={}",JSON.toJSONString(result));
        return result;
    }

}
