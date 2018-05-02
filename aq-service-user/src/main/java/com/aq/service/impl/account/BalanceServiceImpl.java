package com.aq.service.impl.account;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.account.UpdateBalanceDTO;
import com.aq.facade.entity.account.AccountFlow;
import com.aq.facade.entity.manager.Balance;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.entity.permission.User;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.account.IBalanceService;
import com.aq.mapper.account.AccountFlowerMapper;
import com.aq.mapper.manager.BalanceMapper;
import com.aq.mapper.manager.ManagerMapper;
import com.aq.mapper.permission.UserMapper;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额 service.impl
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Slf4j
@Service(version = "1.0.0")
public class BalanceServiceImpl implements IBalanceService {

    @Autowired
    private AccountFlowerMapper accountFlowerMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Result modifyBalance(UpdateBalanceDTO updateBalanceDTO) {
        log.info("修改余额入参：updateBalanceDTO={}", JSON.toJSONString(updateBalanceDTO));
        Result result;
        String message = updateBalanceDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            // 修改余额
            updateBalance(updateBalanceDTO);
            // 流水
            BigDecimal changeMoney = updateBalanceDTO.getChangeBalance().multiply(new BigDecimal(updateBalanceDTO.getTransCodeEnum().getMark()));
            AccountFlow accountFlow = new AccountFlow();
            accountFlow.setTradeCode(updateBalanceDTO.getTransCodeEnum().getCode());
            accountFlow.setOrderNo(updateBalanceDTO.getOrderNo());
            accountFlow.setCreaterId(updateBalanceDTO.getManagerId());
            //accountFlow.setPrice(changeMoney);
            accountFlow.setFlowNo(IdGenerator.nextId(OrderBizCode.ACCOUNT_FLOWER_DD));
            accountFlow.setRoleType(updateBalanceDTO.getRoleType());
            accountFlow.setCreateTime(new Date());
            accountFlow.setRemark(updateBalanceDTO.getRemark());
            if (RoleCodeEnum.MANAGER.getCode().equals(updateBalanceDTO.getRoleType())) {
                accountFlow.setManagerBalance(changeMoney);
                accountFlow.setManagerId(updateBalanceDTO.getManagerId());
                Manager manager = managerMapper.selectByPrimaryKey(updateBalanceDTO.getManagerId());
                accountFlow.setIdCode(manager.getIdCode());
            } else if (RoleCodeEnum.ADMIN.getCode().equals(updateBalanceDTO.getRoleType())) {
                accountFlow.setUserBalance(changeMoney);
                accountFlow.setUserId(updateBalanceDTO.getManagerId());
                User user = userMapper.selectByPrimaryKey(updateBalanceDTO.getManagerId());
                accountFlow.setEmployeeID(user.getEmployeeID());
            }
            accountFlowerMapper.insert(accountFlow);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("修改余额返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改余额异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Result modifyBalanceNoFlow(UpdateBalanceDTO updateBalanceDTO) {
        log.info("修改余额（无流水）入参：updateBalanceDTO={}", JSON.toJSONString(updateBalanceDTO));
        Result result;
        String message = updateBalanceDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            updateBalance(updateBalanceDTO);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("修改余额（无流水）返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改余额（无流水）异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result getBalance(Integer accountId, Byte roleType) {
        log.info("获取余额信息入参：accountId={}，roleType={}", accountId, roleType);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (null == accountId || null == roleType) {
            return ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        }
        try {
            Balance balance = new Balance();
            balance.setManagerId(accountId);
            balance.setRoleType(roleType);
            balance = balanceMapper.selectOne(balance);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, balance);
        } catch (Exception e) {
            log.error("获取余额信息异常：e={}", e);
        }
        log.info("获取余额信息返回值：result={}", JSON.toJSONString(result));
        return result;
    }


    private void updateBalance(UpdateBalanceDTO updateBalanceDTO) {
        // 修改余额
        int count = 0;
        int mark = updateBalanceDTO.getTransCodeEnum().getMark();
        if (mark == -1) {
            count = balanceMapper.reduceBalance(
                    CollectionUtils.toMap(
                            "managerId", updateBalanceDTO.getManagerId()
                            , "balance", updateBalanceDTO.getChangeBalance()
                            , "roleType", updateBalanceDTO.getRoleType()));
        } else if (mark == 1) {
            count = balanceMapper.increaseBalance(
                    CollectionUtils.toMap(
                            "managerId", updateBalanceDTO.getManagerId()
                            , "balance", updateBalanceDTO.getChangeBalance()
                            , "roleType", updateBalanceDTO.getRoleType())
            );
        }
        if (count <= 0) {
            throw new UserBizException("扣除余额失败");
        }
    }


}
