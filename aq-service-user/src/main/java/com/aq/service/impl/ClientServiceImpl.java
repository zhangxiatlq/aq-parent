package com.aq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.PageConstant;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.facade.entity.AuthUser;
import com.aq.facade.entity.ManagerUserRelation;
import com.aq.facade.entity.UserInfo;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.exception.permission.UserExceptionEnum;
import com.aq.facade.service.IClientService;
import com.aq.facade.vo.ClientDetailVO;
import com.aq.facade.vo.ClientListVO;
import com.aq.mapper.AuthUserMapper;
import com.aq.mapper.ManagerUserRelationMapper;
import com.aq.mapper.UserInfoMapper;
import com.aq.util.encrypt.SHA256Python;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户service接口实现
 * @作者： 张霞
 * @创建时间： 17:33 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Service(version="1.0.0",timeout = 60000)
public class ClientServiceImpl implements IClientService {

    @Autowired
    private AuthUserMapper authUserMapper;
    
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ManagerUserRelationMapper managerUserRelationMapper;

	@Override
	public Result getUserInfoByOpenId(String openId) {
		log.info("根据微信openId 得到用户信息入参={}", openId);
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			if (null == openId) {
				result.setMessage("openId 不能为空");
				return result;
			}
			result.setData(authUserMapper.selectUserInfoByOpenId(openId));
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("根据微信openId 得到用户信息异常", e);
		}
		return result;
	}

    /**
     * @author: 张霞
     * @description：翻页获取客户列表
     * @createDate: 17:31 2018/1/20
     * @param pagebean
     * @param selectClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getClientListByPage(PageBean pagebean, SelectClientDTO selectClientDTO) {
        log.info("获取客户列表入参参数pageBean={},selectClientDTO={}",JSON.toJSONString(pagebean), JSON.toJSONString(selectClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pagebean != null) {
                pagebean.setPageSize(pagebean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pagebean.getPageSize());
                pagebean.setPageNum(pagebean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pagebean.getPageNum());
                PageHelper.startPage(pagebean.getPageNum(), pagebean.getPageSize());
            }
            List<ClientListVO> clientListVOS = authUserMapper.getClientListByPage(selectClientDTO);
            if (!RoleCodeEnum.ADMIN.getCode().equals(selectClientDTO.getRoleCode())){
                //不是超级管理员需要隐藏电话号码
                for (ClientListVO clientListVO :
                        clientListVOS ) {
                    clientListVO.setTelphone(StringTools.telphoneChange(clientListVO.getTelphone()));
                }
            }
            PageInfo<ClientListVO> pageInfo = new PageInfo<>(clientListVOS);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.info("查询异常e={}",e);
            result.setMessage("查询异常");
        }
        log.info("获取客户列表处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: 张霞
     * @description：获取客户详情信息
     * @createDate: 19:18 2018/1/20
     * @param selectClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getClientDetail(SelectClientDTO selectClientDTO) {
        log.info("获取客户详情信息入参参数selectClientDTO = {}",JSON.toJSONString(selectClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ClientDetailVO clientDetailVO = authUserMapper.getClientDetail(selectClientDTO);
            if (!RoleCodeEnum.ADMIN.getCode().equals(selectClientDTO.getRoleCode())){
                clientDetailVO.setTelphone(StringTools.telphoneChange(clientDetailVO.getTelphone()));
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,clientDetailVO);
        } catch (Exception e) {
           log.info("查询客户详情异常e={}",e);
        }
        log.info("获取客户详情信息处理结果 = {}",JSON.toJSONString(result));
        return result;
    }

	@Override
	public Result checkBind(String openId) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			UserInfo info = new UserInfo();
			info.setOpenid(openId);
			info = userInfoMapper.selectOne(info);
			if (null != info) {
				result = ResultUtil.getResult(RespCode.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.info("查询用户是否绑定微信异常", e);
		}
		return result;
	}

    /**
     * @author: 张霞
     * @description：客户编辑信息
     * @createDate: 11:13 2018/1/21
     * @param updateClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Transactional
    @Override
    public Result editClient(UpdateClientDTO updateClientDTO) {
        log.info("客户编辑信息入参参数updateClientDTO = {}",JSON.toJSONString(updateClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //第一步：更新用户信息
            AuthUser authUser = authUserMapper.selectByPrimaryKey(updateClientDTO.getId());
            if (Objects.isNull(authUser)){
                log.info("编辑客户时，非法操作，没有相关用户存在authUser表中");
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
                return result;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(updateClientDTO.getId());
            userInfo =userInfoMapper.selectOne(userInfo);
//            userInfo.setFortunellavenosa(updateClientDTO.getAccout());
//            userInfo.setName(updateClientDTO.getName());
            userInfo.setRemark(updateClientDTO.getRemark());
            if (userInfoMapper.updateByPrimaryKeySelective(userInfo)>0){
                result = updateManagerUserRelation(updateClientDTO);
            }else {
                log.info("编辑客户信息时，更新用户资金的时候失败");
            }
        } catch (Exception e) {
            log.info("客户编辑service处理异常，e={}",e);
            throw new UserBizException(UserExceptionEnum.UPDATE_USER_EXCEPTION);
        }
        log.info("客户编辑service处理结果result={}",JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: 张霞
     * @description：客户重置密码
     * @createDate: 14:55 2018/1/22
     * @param updateClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result resetPWD(UpdateClientDTO updateClientDTO) {
       log.info("客户重置密码入参参数updateClientDTO={}",JSON.toJSONString(updateClientDTO));
       Result result = ResultUtil.getResult(RespCode.Code.FAIL);
       try {
           AuthUser authUser = authUserMapper.selectByPrimaryKey(updateClientDTO.getId());
           if (Objects.isNull(authUser)){
               result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
           }else {
               authUser.setPassword(SHA256Python.encode("123456"));
               if (authUserMapper.updateByPrimaryKeySelective(authUser)>0){
                   result = ResultUtil.getResult(RespCode.Code.SUCCESS);
               }else {
                   log.info("客户重置密码失败");
                   result.setMessage("客户重置密码失败");
               }
           }

       }catch (Exception e){
            log.info("客户重置密码异常e={}",e);
       }
       log.info("客户重置密码处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: 张霞
     * @description：更新客户和客户经理关联表
     * @createDate: 19:14 2018/1/21
     * @param updateClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    private Result updateManagerUserRelation(UpdateClientDTO updateClientDTO){
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        //更新资产成功，然后更新关联关系表
        ManagerUserRelation managerUserRelation ;
        managerUserRelation = new ManagerUserRelation();
        managerUserRelation.setUserId(updateClientDTO.getId());
        managerUserRelation = managerUserRelationMapper.selectOne(managerUserRelation);
        if (Objects.isNull(managerUserRelation)){
            if (Objects.nonNull(updateClientDTO.getManagerId())){
                //没有关联关系，新添加关联关系
                log.info("需要新添加客户和客户经理之间关联关系，客户id={}，客户经理id={}",updateClientDTO.getId(),updateClientDTO.getManagerId());
                managerUserRelation = new ManagerUserRelation();
                managerUserRelation.setHeadId(updateClientDTO.getManagerId());
                managerUserRelation.setUserId(updateClientDTO.getId());
                managerUserRelation.setCreateTime(new Date());
                if (managerUserRelationMapper.insert(managerUserRelation)>0){
                    log.info("添加客户和客户经理之间关联关系成功");
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }else {
                    log.info("添加客户和客户经理之间关联关系失败");
                    throw new UserBizException(UserExceptionEnum.UPDATE_CLIENT_ADD_RELATION_FAIL);
                }
            }else {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }
        }else {
            log.info("原始客户和客户经理关系数据managerUserRelation={}",JSON.toJSONString(managerUserRelation));
            if (managerUserRelationMapper.updateManagerUserRelation(updateClientDTO)>0){
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }else {
                throw new RuntimeException("编辑客户信息时，更新客户和客户经理关联关系表失败");
            }
            log.info("更新客户和客户经理关联表处理结果result={}",JSON.toJSONString(result));
        }
        return result;
    }

}
