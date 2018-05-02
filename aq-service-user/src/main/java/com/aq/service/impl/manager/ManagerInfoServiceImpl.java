package com.aq.service.impl.manager;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.authentication.PersonalAuthUtil;
import com.aq.core.authentication.dto.PersonalAuthDTO;
import com.aq.facade.dto.manage.BindBankDTO;
import com.aq.facade.dto.manage.UpdateManagerDTO;
import com.aq.facade.entity.manager.BaseBank;
import com.aq.facade.entity.manager.BaseCardNo;
import com.aq.facade.entity.manager.BindBank;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.enums.customer.GroupEnum;
import com.aq.facade.enums.manager.ManagerEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.manager.IManagerInfoService;
import com.aq.mapper.manager.BaseBankMapper;
import com.aq.mapper.manager.BaseCardNoMapper;
import com.aq.mapper.manager.BindBankMapper;
import com.aq.mapper.manager.ManagerMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 客户经理service.impl
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Slf4j
@Service(version = "1.0.0")
public class ManagerInfoServiceImpl implements IManagerInfoService {

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private BindBankMapper bindBankMapper;

    @Autowired
    private BaseBankMapper baseBankMapper;

    @Autowired
    private BaseCardNoMapper baseCardNoMapper;


    @Override
    public Result getManagerInfo(Integer managerId) {
        log.info("通过客户经理id查询客户经理基本信息(包含银行卡、实名认证）入参：managerId={}", managerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, managerMapper.selectManagerInfoById(managerId));
        } catch (Exception e) {
            log.error("通过客户经理id查询客户经理基本信息(包含银行卡、实名认证）异常：e={}", e);
        }
        log.info("通过客户经理id查询客户经理基本信息(包含银行卡、实名认证）返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result bindBankForManager(BindBankDTO bindBankDTO) {
        log.info("绑定银行卡入参：bindBankDTO={}", JSON.toJSONString(bindBankDTO));
        Result result;
        String message = bindBankDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            //调用第三方地址进行银行卡身份证验证
            PersonalAuthDTO personalAuthDTO = new PersonalAuthDTO();
            personalAuthDTO.setBankCard(bindBankDTO.getBankcard());
            personalAuthDTO.setCardNo(bindBankDTO.getCardNo());
            personalAuthDTO.setRealName(bindBankDTO.getRealName());
            result = PersonalAuthUtil.personalAuthentication(personalAuthDTO);
            if (!result.isSuccess()) {
                return result;
            }
            //银行卡基础信息保存
            String content = result.getData().toString();
            JSONObject obj = JSON.parseObject(content);
            JSONObject information = JSON.parseObject(obj.get("information").toString());
            BaseBank baseBank = JSON.toJavaObject(information, BaseBank.class);
            BaseBank queryBank = new BaseBank();
            queryBank.setCardPrefixLength(baseBank.getCardPrefixLength());
            queryBank.setCardPrefixNum(baseBank.getCardPrefixNum());
            queryBank = baseBankMapper.selectOne(queryBank);
            Integer baseId;
            if (null == queryBank) {
                //新增基础信息（数据收集）
                baseBank.setCreateTime(new Date());
                baseBankMapper.insertUseGeneratedKeys(baseBank);
                baseId = baseBank.getId();
            } else {
                baseId = queryBank.getId();
            }
            //保存身份证认证信息（数据收集）
            BaseCardNo baseCardNo = new BaseCardNo();
            baseCardNo.setCardNo(bindBankDTO.getCardNo());
            if (null == baseCardNoMapper.selectOne(baseCardNo)) {
                baseCardNo.setRealName(bindBankDTO.getRealName());
                baseCardNoMapper.insert(baseCardNo);
            }

            BindBank bindBank = new BindBank();
            bindBank.setManagerId(bindBankDTO.getManagerId());
            bindBank.setIsDelete(GroupEnum.NO_DELETE.getStatus());
            bindBank = bindBankMapper.selectOne(bindBank);
            if (null == bindBank) {
                //新增
                bindBank = new BindBank();
                BeanUtils.copyProperties(bindBankDTO, bindBank);
                bindBank.setIsDelete(GroupEnum.NO_DELETE.getStatus());
                bindBank.setCreaterId(bindBankDTO.getManagerId());
                bindBank.setCreateTime(new Date());
                bindBank.setBankBaseId(baseId);
                bindBankMapper.insertUseGeneratedKeys(bindBank);
            } else {
                //修改
                Integer id = bindBank.getId();
                bindBank = new BindBank();
                BeanUtils.copyProperties(bindBankDTO, bindBank);
                bindBank.setUpdaterId(bindBankDTO.getManagerId());
                bindBank.setUpdateTime(new Date());
                bindBank.setId(id);
                bindBank.setBankBaseId(baseId);
                bindBankMapper.updateByPrimaryKeySelective(bindBank);
            }
            //修改客户经理认证数据
            Manager manager = new Manager();
            manager.setId(bindBankDTO.getManagerId());
            manager.setIsAuthentication(ManagerEnum.AUTHEN.getStatus());
            manager.setIsBindBank(ManagerEnum.BINDBANK.getStatus());
            manager.setRealName(bindBank.getRealName());
            managerMapper.updateByPrimaryKeySelective(manager);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("绑定银行卡返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("绑定银行卡异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyManager(UpdateManagerDTO updateManagerDTO) {
        log.info("修改客户经理信息入参={}", JSON.toJSONString(updateManagerDTO));
        Result result;
        String message = updateManagerDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            Manager manager = new Manager();
            manager.setId(updateManagerDTO.getManagerId());
            BeanUtils.copyProperties(updateManagerDTO, manager);
            managerMapper.updateByPrimaryKeySelective(manager);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            return result;
        } catch (Exception e) {
            log.error("修改客户经理信息异常,e={}", e);
            throw new UserBizException(RespCode.Code.SUCCESS);
        }
    }

}
