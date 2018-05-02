package com.aq.service.impl.order;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.OrderStateEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.TransCodeEnum;
import com.aq.core.lock.RedisDistributionLock;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.dto.AdviserUpdateOrderDTO;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.facade.dto.account.UpdateBalanceDTO;
import com.aq.facade.dto.customer.VipPurchaseDTO;
import com.aq.facade.dto.order.AsyncOrderDTO;
import com.aq.facade.dto.order.UpdateOrderDTO;
import com.aq.facade.dto.order.UpdateVipOrderDTO;
import com.aq.facade.entity.AdviserOrder;
import com.aq.facade.entity.StrategyOrder;
import com.aq.facade.entity.account.AccountFlow;
import com.aq.facade.entity.account.AddAccountFlowerDTO;
import com.aq.facade.entity.customer.Customer;
import com.aq.facade.entity.customer.VipOrder;
import com.aq.facade.entity.customer.VipPurchaseRecord;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.entity.permission.User;
import com.aq.facade.enums.customer.CustomerEnum;
import com.aq.facade.enums.customer.VipOrderStatusEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.IAdviserService;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.service.account.IBalanceService;
import com.aq.facade.service.order.IOrderService;
import com.aq.facade.vo.customer.DivideScaleVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.mapper.account.AccountFlowerMapper;
import com.aq.mapper.customer.*;
import com.aq.mapper.manager.ManagerMapper;
import com.aq.mapper.order.OrderMapper;
import com.aq.mapper.permission.UserMapper;
import com.aq.util.date.DateUtil;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @ClassName: OrderServiceImpl
 * @Description: 订单服务实现
 * @author: lijie
 * @date: 2018年1月29日 下午2:49:35
 */
@Slf4j
@Service(version = "1.0.0")
public class OrderServiceImpl implements IOrderService {

    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);

    @Autowired
    private OrderMapper orderMapper;
    /**
     * vip订单数据操作
     */
    @Autowired
    private VipOrderMapper vipOrderMapper;

    @Autowired
    private ICacheService cacheService;

    @Autowired
    private VipPurchaseRecordMapper vipPurchaseRecordMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private CustomerManagerMapper customerManagerMapper;

    @Autowired
    private RedisDistributionLock distributionLock;

    @Autowired
    private IAccountFlowerService accountFlowerService;

    @Autowired
    private ToolbindnumMapper toolbindnumMapper;

    @Autowired
    private AccountFlowerMapper accountFlowerMapper;

    @Reference(version = "1.0.0", check = false)
    private IWechatStrategyService wechatStrategyService;

    @Reference(version = "1.0.0", check = false)
    private IAdviserService adviserService;

    /**
     * @Creater: zhangxia
     * @description：通过主订单号进行订单更新
     * @methodName: updateOrderByMainOrderNo
     * @params: [mainOrderNo]
     * @return: com.aq.util.result.Result
     * @createTime: 14:22 2018-2-13
     */
    @Override
    public Result updateOrderByMainOrderNo(UpdateOrderDTO updateOrderDTO) {
        log.info("通过主订单号进行订单更新service入参参数updateOrderDTO={}", JSON.toJSONString(updateOrderDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (orderMapper.updateOrderByMainOrderNo(updateOrderDTO) > 0) {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            } else {
                result.setMessage("更新数据失败");
            }
        } catch (Exception e) {
            log.info("通过主订单号进行订单更新异常e={}", e);
        }
        log.info("通过主订单号进行订单更新service处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateAsyncOrder(AsyncOrderDTO asyncOrderDTO) {
        log.info("订单异步回调入参, updateOrderDTO={}", JSON.toJSONString(asyncOrderDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
            String orderNo = asyncOrderDTO.getMainOrderNo();
            String orderPrefix = IdGenerator.getBizeCode(orderNo);
            String key = orderNo + OrderBizCode.INFO;
            List<RenewStrategyDTO> renewStrategyDTOS = (List<RenewStrategyDTO>) cacheService.get(key);
            if (CollectionUtils.isEmpty(renewStrategyDTOS)) {
                result.setMessage("redis购买策略数据为空");
                return result;
            }
            //更新订单
            BeanUtils.copyProperties(asyncOrderDTO, updateOrderDTO);
            int count = orderMapper.updateOrderByMainOrderNo(updateOrderDTO);
            if (count <= 0) {
                result.setMessage("更新订单失败");
                return result;
            }
            Result updateResult = null;
            //新增账户记录
            AccountFlow accountFlow = new AccountFlow();
            accountFlow.setThirdTranCode(asyncOrderDTO.getThirdOrderNo());
            accountFlow.setOrderNo(orderNo);
            accountFlow.setCreaterId(renewStrategyDTOS.get(0).getPurchaserId());
            BigDecimal price = asyncOrderDTO.getPrice().divide(DECIMAL_100);
            accountFlow.setPrice(price);
            accountFlow.setFlowNo(IdGenerator.nextId(OrderBizCode.ACCOUNT_FLOWER_DD));
            accountFlow.setCreateTime(new Date());
            if (OrderBizCode.CUSTOMR_RENEW_MD.equals(orderPrefix) || OrderBizCode.CUSTOMR_BUY_MD.equals(orderPrefix)) {
                accountFlow.setRoleType(RoleCodeEnum.CUSTOMER.getCode());
                accountFlow.setRemark(TransCodeEnum.CUSTOMER_BUY_MD.getSource());
                accountFlow.setTradeCode(TransCodeEnum.CUSTOMER_BUY_MD.getCode());
                accountFlow.setManagerId(renewStrategyDTOS.get(0).getRecommendId());
                accountFlow.setManagerBalance(price.multiply(new BigDecimal(TransCodeEnum.CUSTOMER_MANAGER_BUY_MD.getMark())));
                accountFlow.setCustomerId(renewStrategyDTOS.get(0).getPurchaserId());
                accountFlow.setCustomerBalance(price.multiply(new BigDecimal(TransCodeEnum.CUSTOMER_BUY_MD.getMark())));
                Customer customer = customerMapper.selectByPrimaryKey(renewStrategyDTOS.get(0).getPurchaserId());
                accountFlow.setCustomerTelphone(customer.getTelphone());
                Manager manager = managerMapper.selectByPrimaryKey(renewStrategyDTOS.get(0).getRecommendId());
                accountFlow.setIdCode(manager.getIdCode());
                //给客户经理分钱--star
                UpdateBalanceDTO updateBalanceDTO = new UpdateBalanceDTO();
                updateBalanceDTO.setChangeBalance(price);
                updateBalanceDTO.setManagerId(renewStrategyDTOS.get(0).getRecommendId());
                updateBalanceDTO.setTransCodeEnum(TransCodeEnum.CUSTOMER_MANAGER_BUY_MD);
                updateBalanceDTO.setRoleType(RoleCodeEnum.MANAGER.getCode());
                result = balanceService.modifyBalanceNoFlow(updateBalanceDTO);
                //给客户经理分钱--end
                log.info("购买投顾会修改客户经理余额返回数据={}", JSON.toJSONString(result));
                if (!result.isSuccess()) {
                    throw new UserBizException("购买投顾会修改客户经理余额异常");
                }
            } else if (OrderBizCode.MANAGER_RENEW_MD.equals(orderPrefix) || OrderBizCode.MANAGER_BUY_MD.equals(orderPrefix)) {
                accountFlow.setRemark(TransCodeEnum.MANAGER_BUY_MD.getSource());
                accountFlow.setRoleType(RoleCodeEnum.MANAGER.getCode());
                accountFlow.setTradeCode(TransCodeEnum.MANAGER_BUY_MD.getCode());
                accountFlow.setManagerId(renewStrategyDTOS.get(0).getPurchaserId());
                accountFlow.setManagerBalance(price.multiply(new BigDecimal(TransCodeEnum.MANAGER_BUY_MD.getMark())));
                Manager manager = managerMapper.selectByPrimaryKey(renewStrategyDTOS.get(0).getPurchaserId());
                accountFlow.setIdCode(manager.getIdCode());
            }
            accountFlowerMapper.insert(accountFlow);
            if (OrderBizCode.CUSTOMR_RENEW_MD.equals(orderPrefix) || OrderBizCode.MANAGER_RENEW_MD.equals(orderPrefix)) {
                // 更新策略购买记录
                log.info("修改策略接口入参，renewStrategyDTOS={}", JSON.toJSONString(renewStrategyDTOS));
                updateResult = wechatStrategyService.updateRenewStrategy(renewStrategyDTOS);
            } else if (OrderBizCode.MANAGER_BUY_MD.equals(orderPrefix) || OrderBizCode.CUSTOMR_BUY_MD.equals(orderPrefix)) {
                //购买策略添加记录
                log.info("新增策略接口入参，renewStrategyDTOS={}", JSON.toJSONString(renewStrategyDTOS));
                updateResult = wechatStrategyService.insertBuyStrategy(renewStrategyDTOS);
            }
            log.info("更新策略接口返回值 updateResult={}", JSON.toJSONString(updateResult));
            //判断接口是否成功
            if (null == updateResult || !updateResult.isSuccess()) {
                throw new UserBizException("更新策略购买数据异常");
            }
            cacheService.remove(key);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("订单异步回调返回值, result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("订单异步回调异常e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateAsyncAdviserOrder(AsyncOrderDTO asyncOrderDTO) {
        log.info("订单异步回调（投顾）入参, updateOrderDTO={}", JSON.toJSONString(asyncOrderDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String orderNo = asyncOrderDTO.getMainOrderNo();
            String orderPrefix = IdGenerator.getBizeCode(orderNo);
            List<AdviserOrder> list = adviserService.getAdviserOrder(orderNo).getData(List.class);
            log.info("获取投顾订单信息返回值，list={}", JSON.toJSONString(list));
            if (CollectionUtils.isEmpty(list)) {
                result.setMessage("购买投顾数据为空");
                return result;
            }
            if (!OrderStateEnum.PAYING.getCode().equals(list.get(0).getOrderState())) {
                result.setMessage("订单状态异常");
                return result;
            }
            //新增购买记录
            TransCodeEnum transCodeEnum = insertAdviserFlow(asyncOrderDTO, list.get(0), orderPrefix);
            //给客户经理分钱
            modifyBalance(asyncOrderDTO, list.get(0), transCodeEnum);
            //更新订单-投顾到期时间
            AdviserUpdateOrderDTO adviserUpdateOrderDTO = new AdviserUpdateOrderDTO();
            BeanUtils.copyProperties(asyncOrderDTO, adviserUpdateOrderDTO);
            // 更新投顾到期时间
            log.info("修改投顾到期时间接口入参，adviserUpdateOrderDTO={}", JSON.toJSONString(adviserUpdateOrderDTO));
            Result updateResult = adviserService.updateBuyAdviser(adviserUpdateOrderDTO);
            log.info("投顾到期时间接口返回值 updateResult={}", JSON.toJSONString(updateResult));
            //判断接口是否成功
            if (null == updateResult || !updateResult.isSuccess()) {
                throw new UserBizException("更新投顾购买数据异常");
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("订单异步回调（投顾）返回值, result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("订单异步回调（投顾）异常e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    private void modifyBalance(AsyncOrderDTO asyncOrderDTO, AdviserOrder adviserOrder, TransCodeEnum transCodeEnum) {
        BigDecimal price = asyncOrderDTO.getPrice().divide(DECIMAL_100).multiply(new BigDecimal(transCodeEnum.getMark()));
        UpdateBalanceDTO updateBalanceDTO = new UpdateBalanceDTO();
        updateBalanceDTO.setChangeBalance(price);
        updateBalanceDTO.setManagerId(adviserOrder.getManagerId());
        updateBalanceDTO.setTransCodeEnum(transCodeEnum);
        updateBalanceDTO.setRoleType(RoleCodeEnum.MANAGER.getCode());
        updateBalanceDTO.setOrderNo(asyncOrderDTO.getMainOrderNo());
        updateBalanceDTO.setRemark(transCodeEnum.getSource());
        Result result = balanceService.modifyBalance(updateBalanceDTO);
        log.info("购买投顾会修改客户经理余额返回数据={}", JSON.toJSONString(result));
        if (!result.isSuccess()) {
            throw new UserBizException("购买投顾会修改客户经理余额异常");
        }

    }

    private TransCodeEnum insertAdviserFlow(AsyncOrderDTO asyncOrderDTO, AdviserOrder adviserOrder, String orderPrefix) {
        AccountFlow accountFlow = new AccountFlow();
        accountFlow.setThirdTranCode(asyncOrderDTO.getThirdOrderNo());
        accountFlow.setOrderNo(asyncOrderDTO.getMainOrderNo());
        accountFlow.setCreaterId(adviserOrder.getPurchaserId());
        BigDecimal price = asyncOrderDTO.getPrice().divide(DECIMAL_100);
        accountFlow.setPrice(price);
        accountFlow.setFlowNo(IdGenerator.nextId(OrderBizCode.ACCOUNT_FLOWER_DD));
        accountFlow.setCreateTime(new Date());
        accountFlow.setRemark(TransCodeEnum.ADVISER_MANAGER_BUY.getSource());
        TransCodeEnum transCodeEnum = null;
        if (OrderBizCode.CUSTOMER_ADVISER_MD.equals(orderPrefix)) {
            accountFlow.setRoleType(RoleCodeEnum.CUSTOMER.getCode());
            accountFlow.setTradeCode(TransCodeEnum.ADVISER_CUSTOMER_BUY.getCode());
            accountFlow.setCustomerBalance(price.multiply(new BigDecimal(TransCodeEnum.ADVISER_CUSTOMER_BUY.getMark())));
            accountFlow.setCustomerId(adviserOrder.getPurchaserId());
            Customer customer = customerMapper.selectByPrimaryKey(adviserOrder.getPurchaserId());
            accountFlow.setCustomerTelphone(customer.getTelphone());
            transCodeEnum = TransCodeEnum.ADVISER_CUSTOMER_MANAGER_BUY;
        } else if (OrderBizCode.MANAGER_ADVISER_MD.equals(orderPrefix)) {
            accountFlow.setRoleType(RoleCodeEnum.MANAGER.getCode());
            accountFlow.setTradeCode(TransCodeEnum.ADVISER_MANAGER_BUY.getCode());
            accountFlow.setManagerId(adviserOrder.getPurchaserId());
            accountFlow.setManagerBalance(price.multiply(new BigDecimal(TransCodeEnum.ADVISER_MANAGER_BUY.getMark())));
            Manager manager = managerMapper.selectByPrimaryKey(adviserOrder.getPurchaserId());
            accountFlow.setIdCode(manager.getIdCode());
            transCodeEnum = TransCodeEnum.ADVISER_MANAGER_MANAGER_BUY;
        }
        accountFlowerMapper.insert(accountFlow);
        return transCodeEnum;
    }

    /**
     * @Creater: zhangxia
     * @description：通过主订单号获取所有子订单
     * @methodName: getOrdersByMainOrderNo
     * @params: [mainOrderNo]
     * @return: com.aq.util.result.Result
     * @createTime: 15:26 2018-2-13
     */
    @Override
    public Result getOrdersByMainOrderNo(String mainOrderNo) {
        log.info("通过主订单号获取所有子订单入参参数mainOrderNo={}", mainOrderNo);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<StrategyOrder> strategyOrders = orderMapper.selectByExample(mainOrderNo);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, strategyOrders);
        } catch (Exception e) {
            log.info("通过主订单号获取所有子订单处理异常e={}", e);
        }
        log.info("通过主订单号获取所有子订单处理结果result={}", result);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addVipOrder(VipPurchaseDTO dto) {
        log.info("vip购买下单入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            // TODO:
            if (dto.getNum() != 1) {
                result.setMessage("目前只支持购买一个月");
                return result;
            }
            // TODO:目前金额写死、后续修改

            BigDecimal max = new BigDecimal("300");
            BigDecimal price = new BigDecimal(dto.getPrice());
            if (price.compareTo(max) != 0) {
                result.setMessage("支付金额与实际不一致");
                return result;
            }
            VipOrder record = new VipOrder();
            record.setCreaterId(dto.getCustomerId());
            record.setCreateTime(new Date());
            record.setCustomerId(dto.getCustomerId());
            record.setIsDelete(VipOrderStatusEnum.NO_DELETE.getStatus());
            record.setMonth(dto.getNum());
            record.setOrderNo(IdGenerator.nextId(OrderBizCode.CUSTOMR_VIP));
            record.setStatus(OrderStateEnum.PAYING.getCode());
            record.setPrice(price);
            /**
             * modify
             * 获取 分成比例
             * 新增分成比例
             *
             * @author 郑朋
             * @create 2018/3/22
             */
            DivideScaleVO divideScaleVO = getCustomer(dto.getCustomerId());
            if (null != divideScaleVO) {
                record.setDivideScale(divideScaleVO.getDivideScale());
                record.setManagerDivideScale(divideScaleVO.getManagerDivideScale());
                record.setManagerId(divideScaleVO.getManagerId());
                record.setUserId(divideScaleVO.getUserId());
            }
            // 添加订单
            int num = vipOrderMapper.insert(record);
            if (num != 1) {
                throw new UserBizException("添加vip订单记录失败");
            }
            OrderCacheInfoVO info = new OrderCacheInfoVO();
            info.setOrderNo(record.getOrderNo());
            info.setPrice(record.getPrice().toString());
            //info.setPrice("0.1");
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS, info);
        } catch (Exception e) {
            log.error("vip购买下单异常", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    private DivideScaleVO getCustomer(Integer customerId) {
        return customerManagerMapper.selectDivideScale(customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateVipOrder(UpdateVipOrderDTO dto) {
        log.info("更新vip购买订单入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String errorStr = dto.validateForm();
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        try {
            String lockKey = "CALLBACK_" + dto.getOrderNo();
            try {
                distributionLock.lock(lockKey);
                VipOrder record = new VipOrder();
                record.setOrderNo(dto.getOrderNo());
                record.setIsDelete(VipOrderStatusEnum.NO_DELETE.getStatus());
                record.setStatus(OrderStateEnum.PAYING.getCode());
                record = vipOrderMapper.selectOne(record);
                if (null == record) {
                    result.setMessage("订单编号：" + dto.getOrderNo() + ",不存在");
                    return result;
                }
                Customer crecord = customerMapper.selectByPrimaryKey(record.getCustomerId());
                if (null == crecord) {
                    log.warn("客户:" + record.getCustomerId() + ",数据不存在");
                    result.setMessage("客户数据不存在");
                    return result;
                }
                updateVipOrder(dto, record);
                updateCustomer(crecord, dto.getUpdaterId());
                Map<String, BigDecimal> map = updateBalance(record);
                genOrderFlow(dto, record, map);
                // TODO:修改tool推荐数量(数量目前写死)
                updateToolBindNum(crecord.getId(), 29);
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            } finally {
                distributionLock.unLock(lockKey);
            }
        } catch (Exception e) {
            log.error("更新vip购买订单异常", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param @param customer    参数
     * @return void    返回类型
     * @throws
     * @Title: updateToolBindNum
     * @Description: 修改客户最大推荐数量
     */
    private void updateToolBindNum(Integer customerId, Integer num) {
        List<Integer> list = new ArrayList<>();
        list.add(customerId);
        int ret = toolbindnumMapper.updateToolBindNumByCusId(list, num);
        if (ret <= 0) {
            throw new UserBizException("修改客户最大推荐数量失败");
        }
    }

    /**
     * @param @param  crecord
     * @param @param  updaterId
     * @param @throws ParseException    参数
     * @return void    返回类型
     * @throws
     * @Title: updateCustomer
     * @Description: 修改客户
     */
    private void updateCustomer(Customer crecord, Integer updaterId) throws ParseException {
        // 修改客户vip到期时间
        Customer cupdate = new Customer();
        cupdate.setUpdateTime(new Date());
        cupdate.setUpdaterId(updaterId);
        Date localTime = new Date();
        if (CustomerEnum.YES_VIP.getStatus().equals(crecord.getIsVIP())) {
            // 计算时间
            if (crecord.getEndTime().getTime() > localTime.getTime()) {
                localTime = crecord.getEndTime();
            }
        } else {
            cupdate.setIsVIP(CustomerEnum.YES_VIP.getStatus());
        }
        cupdate.setEndTime(DateUtil.addMonth(localTime));
        cupdate.setId(crecord.getId());
        customerMapper.updateByPrimaryKeySelective(cupdate);
    }


    /**
     * vip 购买分成
     *
     * @param record
     * @return void
     * @author 郑朋
     * @create 2018/3/22
     */
    private Map<String, BigDecimal> updateBalance(VipOrder record) {
        Map<String, BigDecimal> map = new HashMap<>(16);
        UpdateBalanceDTO updateBalanceDTO = new UpdateBalanceDTO();
        BigDecimal managerMoney, userMoney, aqMoney = BigDecimal.ZERO;
        if (record.getManagerId() == null) {
            return map;
        }
        if (null != record.getUserId() && record.getDivideScale() != 0) {
            aqMoney = record.getPrice().multiply(new BigDecimal(record.getDivideScale()));
            map.put("aqMoney", aqMoney);
        }

        if (null != record.getManagerDivideScale()) {
            //客户经理分成
            Result result;
            managerMoney = record.getPrice().subtract(aqMoney).multiply(new BigDecimal(record.getManagerDivideScale()));
            if (managerMoney.compareTo(BigDecimal.ZERO) == 1) {
                updateBalanceDTO.setChangeBalance(managerMoney);
                updateBalanceDTO.setManagerId(record.getManagerId());
                updateBalanceDTO.setRoleType(RoleCodeEnum.MANAGER.getCode());
                updateBalanceDTO.setTransCodeEnum(TransCodeEnum.BALANCE_VIP_CASH);
                result = balanceService.modifyBalanceNoFlow(updateBalanceDTO);
                log.info("vip充值到账修改客户经理余额返回数据={}", JSON.toJSONString(result));
                if (!result.isSuccess()) {
                    throw new UserBizException("vip充值到账修改客户经理余额异常");
                }
                map.put("managerMoney", managerMoney);
            }
            // 员工分成
            if (null != record.getUserId()) {
                userMoney = record.getPrice().subtract(aqMoney).subtract(managerMoney);
                if (userMoney.compareTo(BigDecimal.ZERO) == 1) {
                    updateBalanceDTO.setChangeBalance(userMoney);
                    updateBalanceDTO.setManagerId(record.getUserId());
                    updateBalanceDTO.setRoleType(RoleCodeEnum.ADMIN.getCode());
                    updateBalanceDTO.setTransCodeEnum(TransCodeEnum.BALANCE_VIP_CASH);
                    result = balanceService.modifyBalanceNoFlow(updateBalanceDTO);
                    log.info("vip充值到账修改员工经理余额返回数据={}", JSON.toJSONString(result));
                    if (!result.isSuccess()) {
                        throw new UserBizException("vip充值到账修改员工经理余额异常");
                    }
                    map.put("userMoney", userMoney);
                }
            }
        }
        return map;
    }

    /**
     * @param @param dto
     * @param @param record    参数
     * @return void    返回类型
     * @throws
     * @Title: updateOrder
     * @Description: 修改vip订单
     */
    private void updateVipOrder(UpdateVipOrderDTO dto, VipOrder record) {
        // 更新订单状态
        VipOrder update = new VipOrder();
        update.setId(record.getId());
        update.setUpdaterId(dto.getUpdaterId());
        update.setUpdateTime(new Date());
        update.setPayType(dto.getPayType());
        update.setStatus(dto.getStatus());
        vipOrderMapper.updateByPrimaryKeySelective(update);
    }

    /**
     * @param @param dto
     * @param @param record    参数
     * @return void    返回类型
     * @throws
     * @Title: genOrderFlow
     * @Description: 生成订单流水
     */
    private void genOrderFlow(UpdateVipOrderDTO dto, VipOrder record, Map<String, BigDecimal> map) {
        // 生成订单流水
        VipPurchaseRecord vipRecord = new VipPurchaseRecord();
        vipRecord.setCreateTime(new Date());
        vipRecord.setCustomerId(record.getCustomerId());
        vipRecord.setOrderId(record.getId());
        vipRecord.setOrderNo(record.getOrderNo());
        vipRecord.setPrice(record.getPrice());
        vipRecord.setThirdOrderNo(dto.getThirdOrderNo());
        vipRecord.setStatus(dto.getStatus());
        vipPurchaseRecordMapper.insert(vipRecord);

        // 生成总的用户流水
        AddAccountFlowerDTO addAccountFlowerDTO = new AddAccountFlowerDTO();
        addAccountFlowerDTO.setCreaterId(record.getCustomerId());
        addAccountFlowerDTO.setOrderNo(record.getOrderNo());
        addAccountFlowerDTO.setPrice(record.getPrice());
        addAccountFlowerDTO.setRemark(TransCodeEnum.BALANCE_VIP_CASH.getMsg());
        addAccountFlowerDTO.setRoleType(RoleCodeEnum.CUSTOMER.getCode());
        addAccountFlowerDTO.setTradeCode(TransCodeEnum.BALANCE_VIP_CASH.getCode());
        addAccountFlowerDTO.setThirdTranCode(dto.getThirdOrderNo());
        addAccountFlowerDTO.setUserBalance(map.get("userMoney"));
        addAccountFlowerDTO.setUserId(record.getUserId());
        addAccountFlowerDTO.setManagerBalance(map.get("managerMoney"));
        addAccountFlowerDTO.setManagerId(record.getManagerId());
        addAccountFlowerDTO.setCustomerId(record.getCustomerId());
        addAccountFlowerDTO.setCustomerBalance(record.getPrice().multiply(new BigDecimal(-1)));
        addAccountFlowerDTO.setAqDivide(map.get("aqMoney"));
        Customer customer = customerMapper.selectByPrimaryKey(record.getCustomerId());
        addAccountFlowerDTO.setCustomerTelphone(customer.getTelphone());
        if (null != record.getManagerId()) {
            Manager manager = managerMapper.selectByPrimaryKey(record.getManagerId());
            addAccountFlowerDTO.setIdCode(manager.getIdCode());
        }
        if (null != record.getUserId()) {
            User user = userMapper.selectByPrimaryKey(record.getUserId());
            addAccountFlowerDTO.setEmployeeID(user.getEmployeeID());
        }
        Result resut = accountFlowerService.addAccountFlower(addAccountFlowerDTO);

        log.info("vip充值到账生成账户流水返回数据={}", JSON.toJSONString(resut));
        if (!resut.isSuccess()) {
            throw new UserBizException("vip充值到账生成账户流水异常");
        }
    }

    @Override
    public Result getVipOrderInfo(Object obj) {
        log.info("根据订单号/订单ID查询订单信息入参={}", obj);
        try {
            VipOrder re = null;
            VipOrder record = new VipOrder();
            record.setIsDelete(VipOrderStatusEnum.NO_DELETE.getStatus());
            if (obj instanceof Number) {
                record.setId((int) obj);
                re = vipOrderMapper.selectOne(record);
            } else if (obj instanceof String) {
                record.setOrderNo(obj.toString());
                re = vipOrderMapper.selectOne(record);
            }
            return ResultUtil.getResult(RespCode.Code.SUCCESS, re);
        } catch (Exception e) {
            log.info("根据订单号/订单ID查询订单信息异常", e);
            return ResultUtil.getResult(RespCode.Code.FAIL);
        }
    }
}
