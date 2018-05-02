package com.aq.service.impl.account;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.CashAuthStatusEnum;
import com.aq.core.constant.IsDeleteEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.TransCodeEnum;
import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.dto.account.DrawBalanceDTO;
import com.aq.facade.entity.account.AccountFlow;
import com.aq.facade.entity.account.AddAccountFlowerDTO;
import com.aq.facade.entity.account.DrawcashApply;
import com.aq.facade.entity.manager.Balance;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.vo.account.AccountFlowerVO;
import com.aq.facade.vo.account.DrawCashVO;
import com.aq.facade.vo.account.ManagerFlowerVO;
import com.aq.mapper.account.AccountFlowerMapper;
import com.aq.mapper.account.DrawcashApplyMapper;
import com.aq.mapper.manager.BalanceMapper;
import com.aq.util.encrypt.SHA256Python;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账户流水 service.impl
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
@Slf4j
@Service(version = "1.0.0")
public class AccountFlowerServiceImpl implements IAccountFlowerService {

    @Autowired
    private AccountFlowerMapper accountFlowerMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private DrawcashApplyMapper drawcashApplyMapper;


    @Override
    public Result getAccountFlower(PageBean pageBean, AccountFlowerDTO accountFlowerDTO) {
        log.info("获取账户流水入参：pageBean={},accountFlowerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(accountFlowerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            //查询余额
            ManagerFlowerVO managerFlowerVO = new ManagerFlowerVO();
            Balance balance = new Balance();
            balance.setManagerId(accountFlowerDTO.getAccountId());
            balance.setRoleType(accountFlowerDTO.getRoleType());
            balance = balanceMapper.selectOne(balance);
            managerFlowerVO.setBalance(balance.getMoney().toString());
            managerFlowerVO.setTotalRevenue(balance.getTotalRevenue().toString());
            managerFlowerVO.setTotalSettlement(balance.getTotalSettlement().toString());
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<AccountFlowerVO> list = accountFlowerMapper.selectFlower(accountFlowerDTO);
            PageInfo<AccountFlowerVO> pageInfo = new PageInfo<>(list);
            managerFlowerVO.setList(pageInfo.getList());
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, managerFlowerVO, pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取账户流水异常：e={}", e);
        }
        log.info("获取账户流水返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getAccountFlowerList(PageBean pageBean, AccountFlowerDTO accountFlowerDTO) {
        log.info("获取账户流水集合入参：pageBean={},accountFlowerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(accountFlowerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<AccountFlowerVO> list = accountFlowerMapper.selectFlower(accountFlowerDTO);
            PageInfo<AccountFlowerVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取账户流水集合异常：e={}", e);
        }
        log.info("获取账户流水集合返回值：result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 获取所有账户流水
     * @params: [pageBean, accountFlowerDTO]
     * @methodName:getAllAccountFlowerList
     * @date: 2018/3/26 0026 下午 16:45
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result getAllAccountFlowerList(PageBean pageBean, AccountFlowerDTO accountFlowerDTO) {
        log.info("获取所有账户流水集合入参：pageBean={},accountFlowerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(accountFlowerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<AccountFlowerVO> list = accountFlowerMapper.allSelectFlower(accountFlowerDTO);
            PageInfo<AccountFlowerVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取所有账户流水集合异常：e={}", e);
        }
        log.info("获取所有账户流水集合返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result drawBalance(DrawBalanceDTO drawBalanceDTO) {
        log.info("提现申请入参：drawBalanceDTO={}", JSON.toJSONString(drawBalanceDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String message = drawBalanceDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            //银行卡绑定信息
            DrawCashVO drawCashVO = balanceMapper.selectBalanceInfo(drawBalanceDTO.getManagerId());
            if (null == drawCashVO || StringUtils.isBlank(drawCashVO.getBankcard())) {
                result.setMessage("未绑定银行卡");
                return result;
            }
            if (drawBalanceDTO.getDrawCash().compareTo(new BigDecimal(drawCashVO.getBalance())) == 1) {
                result.setMessage("余额不足");
                return result;
            }
            if (StringUtils.isBlank(drawCashVO.getPayPassword())) {
                result.setMessage("支付密码未设置");
                return result;
            }
            if (!SHA256Python.checkPassword(drawBalanceDTO.getPayPassword(), drawCashVO.getPayPassword())) {
                result.setMessage("支付密码不正确");
                return result;
            }
            DrawcashApply drawcashApply = new DrawcashApply();
            String orderNo = IdGenerator.nextId(OrderBizCode.DRAW_CASH_DD);
            drawcashApply.setStatus(CashAuthStatusEnum.CASH_AUTHING_ENUM.getCode());
            drawcashApply.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            drawcashApply.setOrderNo(orderNo);
            drawcashApply.setCreaterId(drawBalanceDTO.getManagerId());
            drawcashApply.setCreateTime(new Date());
            drawcashApply.setPrice(drawBalanceDTO.getDrawCash());
            drawcashApply.setBankName(drawCashVO.getBankName());
            drawcashApply.setOpeningAddress(drawCashVO.getOpeningAddress());
            drawcashApply.setBankNo(drawCashVO.getBankcard());
            drawcashApply.setRequestIp(drawBalanceDTO.getRequestIp());
            drawcashApplyMapper.insertUseGeneratedKeys(drawcashApply);
            //流水
            AccountFlow accountFlow = new AccountFlow();
            BigDecimal changeMoney = drawBalanceDTO.getDrawCash().multiply(new BigDecimal(TransCodeEnum.BALANCE_DRAW_CASH.getMark()));
            accountFlow.setTradeCode(TransCodeEnum.BALANCE_DRAW_CASH.getCode());
            accountFlow.setOrderNo(orderNo);
            accountFlow.setCreaterId(drawBalanceDTO.getManagerId());
            accountFlow.setPrice(drawBalanceDTO.getDrawCash());
            accountFlow.setFlowNo(IdGenerator.nextId(OrderBizCode.ACCOUNT_FLOWER_DD));
            accountFlow.setRoleType(RoleCodeEnum.MANAGER.getCode());
            accountFlow.setManagerBalance(changeMoney);
            accountFlow.setCreateTime(new Date());
            accountFlow.setManagerId(drawBalanceDTO.getManagerId());
            accountFlowerMapper.insert(accountFlow);
            //扣除余额
            int count = balanceMapper.reduceBalance(CollectionUtils.toMap(
                    "managerId", drawBalanceDTO.getManagerId()
                    , "balance", drawBalanceDTO.getDrawCash()
                    , "roleType", RoleCodeEnum.MANAGER.getCode()));
            if (count <= 0) {
                throw new UserBizException("扣除余额失败");
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("提现申请返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("提现申请异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result getBalanceInfo(Integer managerId) {
        log.info("余额信息入参：managerId={}", managerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //查询余额
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, balanceMapper.selectBalanceInfo(managerId));
        } catch (Exception e) {
            log.error("余额信息异常：e={}", e);
        }
        log.info("余额信息返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Result addAccountFlower(AddAccountFlowerDTO addAccountFlowerDTO) {
        log.info("新增账户流水入参：addAccountFlowerDTO={}", JSON.toJSONString(addAccountFlowerDTO));
        Result result;
        try {
            // 查询余额
            AccountFlow accountFlow = new AccountFlow();
            BeanUtils.copyProperties(addAccountFlowerDTO, accountFlow);
            accountFlow.setFlowNo(IdGenerator.nextId(OrderBizCode.ACCOUNT_FLOWER_DD));
            accountFlow.setCreateTime(new Date());
            accountFlowerMapper.insert(accountFlow);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增账户流水返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("新增账户流水异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }
}
