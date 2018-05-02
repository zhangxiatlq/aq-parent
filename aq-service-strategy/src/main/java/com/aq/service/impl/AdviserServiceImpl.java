package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.constant.*;
import com.aq.core.exception.BizException;
import com.aq.core.lock.PythonRedisLock;
import com.aq.facade.contant.AdviserConstants;
import com.aq.facade.contant.AdviserEnum;
import com.aq.facade.contant.DirectionEnum;
import com.aq.facade.contant.PushStateEnum;
import com.aq.facade.dto.*;
import com.aq.facade.entity.*;
import com.aq.facade.service.IAdviserService;
import com.aq.facade.vo.*;
import com.aq.mapper.*;
import com.aq.util.date.DateUtil;
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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 投顾会service.impl
 *
 * @author 郑朋
 * @create 2018/3/6 0006
 **/
@Slf4j
@Service(version = "1.0.0")
public class AdviserServiceImpl implements IAdviserService {

    /**
     * python 冻结资金
     */
    private static final String PYTHON_DATA_KEY_FREEZE = "totalFreezeNum";

    /**
     * python 可用资金
     */
    private static final String PYTHON_DATA_KEY_ASSETS = "availableAssets";

    @Autowired
    private AdviserMapper adviserMapper;

    @Autowired
    private AdviserPositionMapper adviserPositionMapper;

    @Autowired
    private AdviserTradeRecordMapper adviserTradeRecordMapper;

    @Autowired
    private AdviserPushMapper adviserPushMapper;

    @Autowired
    private AdviserPushUserMapper adviserPushUserMapper;

    @Autowired
    private AdviserOrderMapper adviserOrderMapper;


    @Autowired
    private AdviserPurchaseMapper adviserPurchaseMapper;

    @Autowired
    private AdviserRecommendMapper adviserRecommendMapper;

    @Autowired
    private StringRedisTemplate pyRedisTemplate;

    @Autowired
    private AdviserPythonService adviserPythonService;

    @Autowired
    private AdviserConfigMapper adviserConfigMapper;

    @Autowired
    private PythonRedisLock pythonRedisLock;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addAdviser(AdviserAddDTO adviserAddDTO) {
        try {
            Result result = ResultUtil.getResult(RespCode.Code.FAIL);
            log.info("新增投顾会入参，adviserAddDTO={}", JSON.toJSONString(adviserAddDTO));
            String message = adviserAddDTO.validateForm();
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            Adviser adviser = new Adviser();
            adviser.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            adviser.setCreaterId(adviserAddDTO.getManagerId());
            //查询模拟盘
            if (CollectionUtils.isNotEmpty(adviserMapper.select(adviser))) {
                result.setMessage("已经存在模拟盘");
                return result;
            }
            Object time = pyRedisTemplate.opsForValue().get(CacheKey.TRADE_DATE.getKey());
            if (null == time) {
                result.setMessage("交易时间数据不存在");
                return result;
            }
            log.info("收盘交易时间（缓存）time={}", time.toString());
            adviser = new Adviser();
            BeanUtils.copyProperties(adviserAddDTO, adviser);
            adviser.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            adviser.setIsImport(AdviserEnum.NOT_IMPORT.getCode());
            adviser.setTotalPrice(adviser.getTotalPrice().multiply(new BigDecimal(10000)));
            adviser.setCreaterId(adviserAddDTO.getManagerId());
            adviser.setCreateTime(new Date());
            adviser.setIsVisible(AdviserEnum.YES_VISIBLE.getCode());
            adviserMapper.insertUseGeneratedKeys(adviser);
            //新增推荐记录（客户经理自己）
            AdviserRecommend adviserRecommend = new AdviserRecommend();
            adviserRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            adviserRecommend.setRecommendedId(adviserAddDTO.getManagerId());
            adviserRecommend.setBeRecommendedId(adviserAddDTO.getManagerId());
            adviserRecommend.setAdviserId(adviser.getId());
            adviserRecommend.setPushPrice(new BigDecimal("0"));
            adviserRecommend.setBeRecommendedRoleType(RoleCodeEnum.MANAGER.getCode());
            adviserRecommend.setCreateTime(new Date());
            adviserRecommend.setPushDate(new Date());
            adviserRecommend.setPushState(PushStateEnum.PUSH.getCode());
            adviserRecommendMapper.insert(adviserRecommend);
            //投顾记录新增一条数据 （缓存-数据库）
            insertTradeRecord(time, adviser);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增投顾会返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("新增投顾会异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    private void insertTradeRecord(Object time, Adviser adviser) throws ParseException {
        List<String> timeList = JSONArray.parseArray(time.toString(), String.class);
        String tradeTime = timeList.get(timeList.size() - 1);
        AdviserTradeRecord adviserTradeRecord = new AdviserTradeRecord();
        adviserTradeRecord.setAdviserId(adviser.getId());
        adviserTradeRecord.setIndexTime(DateUtils.parseDate(tradeTime, DateUtil.YYYYMMDD));
        adviserTradeRecord.setCreateTime(new Date());
        adviserTradeRecord.setCreateId(adviser.getManagerId());
        adviserTradeRecord.setCumulativeIncome(BigDecimal.ZERO);
        adviserTradeRecord.setAnnualIncome(BigDecimal.ZERO);
        adviserTradeRecord.setMaxRetracement(BigDecimal.ZERO);
        adviserTradeRecord.setReturnRetracementRatio(BigDecimal.ZERO);
        adviserTradeRecord.setAdviserPeriod(BigDecimal.ZERO);
        adviserTradeRecord.setSharpRate(BigDecimal.ZERO);
        adviserTradeRecord.setTotalAssets(adviser.getTotalPrice());
        adviserTradeRecord.setAvailableAssets(adviser.getTotalPrice());
        adviserTradeRecord.setMarketValue(BigDecimal.ZERO);
        adviserTradeRecord.setTodayRate(BigDecimal.ZERO);
        adviserTradeRecord.setNumericValue(BigDecimal.ZERO);
        adviserTradeRecord.setVolatility(BigDecimal.ZERO);
        adviserTradeRecord.setTodayRetracement(BigDecimal.ZERO);
        adviserTradeRecordMapper.insertUseGeneratedKeys(adviserTradeRecord);
        JSONObject object = JSON.parseObject(JSON.toJSONString(adviserTradeRecord));
        object.put("initTotalPrice", adviser.getTotalPrice());
        object.put("indexTime", tradeTime);
        object.put("createTime", DateFormatUtils.format(adviserTradeRecord.getCreateTime(), DateUtil.YYYYMMDDHHMMSS));
        pyRedisTemplate.opsForHash().put(CacheKey.ADVISER_TRADE_RECORD.getKey(), AdviserPythonService.getAdviserKey(adviser.getId()),
                object.toJSONString());
        pyRedisTemplate.opsForHash().put(CacheKey.ADVISER_TRADE_RECORD_REALTIME.getKey(), AdviserPythonService.getAdviserKey(adviser.getId()),
                object.toJSONString());
        List<Map<String, Object>> listRecord = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        map.put("indexTime", tradeTime);
        map.put("totalAssets", adviser.getTotalPrice());
        listRecord.add(map);
        pyRedisTemplate.opsForHash().put(CacheKey.DAILY_ADVISER_TRADE_RECORD.getKey(), AdviserPythonService.getAdviserKey(adviser.getId()),
                JSON.toJSONString(listRecord));

    }

    @Override
    public Result getAdviser(Integer managerId) {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        log.info("投顾会详情入参，managerId={}", managerId);
        try {
            AdviserInfoVO adviserInfoVO = adviserMapper.selectAdviserInfo(managerId);
            if (null != adviserInfoVO) {
                adviserInfoVO.setIsTrade(AdviserEnum.NO_TRADE.getCode());
                if (adviserInfoVO.getNum() > 0) {
                    adviserInfoVO.setIsTrade(AdviserEnum.YES_TRADE.getCode());
                    adviserInfoVO.setTotalPrice(null);
                }
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, adviserInfoVO);
            } else {
                result.setMessage("不存在模拟盘");
            }
        } catch (Exception e) {
            log.error("投顾会详情返异常，e={}", e);
        }
        log.info("投顾会详情返回值，result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyAdviser(AdviseUpdateDTO adviseUpdateDTO) {
        try {
            Result result;
            log.info("修改投顾会入参，adviseUpdateDTO={}", JSON.toJSONString(adviseUpdateDTO));
            String message = adviseUpdateDTO.validateForm();
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            boolean flag = null != adviseUpdateDTO.getTotalPrice() &&
                    (adviseUpdateDTO.getTotalPrice().compareTo(BigDecimal.valueOf(1000)) == 1
                            || adviseUpdateDTO.getTotalPrice().compareTo(BigDecimal.valueOf(10)) == -1);
            if (flag) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage("总资产不能小于10万元,不能大于1000万元");
                return result;
            }
            if (null != adviseUpdateDTO.getTotalPrice()) {
                adviseUpdateDTO.setTotalPrice(adviseUpdateDTO.getTotalPrice().multiply(new BigDecimal(10000)));
            }
            adviserMapper.updateAdviserInfo(adviseUpdateDTO);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("修改投顾会返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改投顾会异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteAdviser(Integer managerId) {
        try {
            Result result;
            log.info("删除投顾会入参，managerId={}", managerId);
            if (null == managerId) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                return result;
            }
            Adviser adviser = new Adviser();
            adviser.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            adviser.setCreaterId(managerId);
            adviser = adviserMapper.selectOne(adviser);
            if (null != adviser) {
                Integer adviserId = adviser.getId();
                Adviser updateAdviser = new Adviser();
                //更新导入
                updateAdviser.setId(adviserId);
                updateAdviser.setUpdaterId(managerId);
                updateAdviser.setUpdateTime(new Date());
                updateAdviser.setIsImport(AdviserEnum.NOT_IMPORT.getCode());
                updateAdviser.setCreateTime(new Date());
                adviserMapper.updateByPrimaryKeySelective(updateAdviser);
                //删除推送记录
                AdviserPush adviserPush = new AdviserPush();
                adviserPush.setAdviserId(adviserId);
                List<AdviserPush> list = adviserPushMapper.select(adviserPush);
                if (CollectionUtils.isNotEmpty(list)) {
                    adviserPushUserMapper.deleteAdviserPushUser(list);
                }
                adviserPushMapper.delete(adviserPush);
                //删除持仓
                AdviserPosition adviserPosition = new AdviserPosition();
                adviserPosition.setAdviserId(adviserId);
                List<AdviserPosition> positionList = adviserPositionMapper.select(adviserPosition);
                adviserPositionMapper.delete(adviserPosition);
                //删除投顾指标记录
                AdviserTradeRecord adviserTradeRecord = new AdviserTradeRecord();
                adviserTradeRecord.setAdviserId(adviserId);
                adviserTradeRecordMapper.delete(adviserTradeRecord);
                //删除缓存
                adviserPythonService.deleteAdviser(adviserId, positionList, list);
                Object time = pyRedisTemplate.opsForValue().get(CacheKey.TRADE_DATE.getKey());
                log.info("收盘交易时间（缓存）time={}", time.toString());
                //投顾记录新增一条数据 （缓存-数据库）
                insertTradeRecord(time, adviser);
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("删除投顾会返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("删除投顾会异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getAdviserList(AdviserPageDTO adviserPageDTO, PageBean pageBean) {
        log.info("获取投顾列表（web） 入参：pageBean={},adviserPageDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(adviserPageDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isNotEmpty(adviserPageDTO.getCreateTimeStart())) {
                adviserPageDTO.setCreateTimeStart(adviserPageDTO.getCreateTimeStart() + " 00:00:00");
            }
            if (StringUtils.isNotEmpty(adviserPageDTO.getCreateTimeEnd())) {
                adviserPageDTO.setCreateTimeEnd(adviserPageDTO.getCreateTimeEnd() + " 23:59:59");
            }
            pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
            pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<AdviserPageVO> list = adviserMapper.selectAdviserList(adviserPageDTO);
            PageInfo<AdviserPageVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取投顾列表（web）异常：e={}", e);
        }
        log.info("获取投顾列表（web）返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyAdviserById(AdviserDTO adviserDTO) {
        try {
            log.info("通过投顾id修改投顾信息 入参，adviserDTO={}", JSON.toJSONString(adviserDTO));
            Adviser adviser = new Adviser();
            BeanUtils.copyProperties(adviserDTO, adviser);
            adviserMapper.updateByPrimaryKeySelective(adviser);
            Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("通过投顾id修改投顾信息 返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("通过投顾id修改投顾信息 异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addAdviserOrder(List<RenewAdviserDTO> renewAdviserDTOS) {
        log.info("客户或者客户经理(微信端或者pc端)对投顾进行续费或者购买，renewAdviserDTOS={}", JSON.toJSONString(renewAdviserDTOS));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (CollectionUtils.isEmpty(renewAdviserDTOS)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                return result;
            }
            String mainOrderNo = "", orderNo = "";
            RenewAdviserDTO renewAdviserDTO = renewAdviserDTOS.get(0);
            if (RoleCodeEnum.CUSTOMER.getCode().equals(renewAdviserDTO.getPurchaserType())) {
                if (PurchaseTypeEnum.PURCHASE_RENEW_ENUM.getCode().equals(renewAdviserDTO.getPurchaseType())) {
                    //客户进行续费策略
                    mainOrderNo = OrderBizCode.CUSTOMER_ADVISER_RENEW_MD;
                    orderNo = OrderBizCode.CUSTOMER_ADVISER_RENEW_DD;
                } else {
                    //客户进行购买策略
                    mainOrderNo = OrderBizCode.CUSTOMER_ADVISER_BUY_MD;
                    orderNo = OrderBizCode.CUSTOMER_ADVISER_BUY_DD;
                }
            } else if (RoleCodeEnum.MANAGER.getCode().equals(renewAdviserDTO.getPurchaserType())) {
                if (PurchaseTypeEnum.PURCHASE_RENEW_ENUM.getCode().equals(renewAdviserDTO.getPurchaseType())) {
                    //客户经理进行续费策略
                    mainOrderNo = OrderBizCode.MANAGER_ADVISER_RENEW_MD;
                    orderNo = OrderBizCode.MANAGER_ADVISER_RENEW_DD;
                } else {
                    //客户经理进行购买策略
                    mainOrderNo = OrderBizCode.MANAGER_ADVISER_BUY_MD;
                    orderNo = OrderBizCode.MANAGER_ADVISER_BUY_DD;
                }
            }
            List<AdviserOrder> adviserOrders = new ArrayList<>();
            BigDecimal totalMoney = new BigDecimal("0");
            AdviserOrder adviserOrder;
            //封装list
            mainOrderNo = IdGenerator.nextId(mainOrderNo);
            for (RenewAdviserDTO dto : renewAdviserDTOS) {
                totalMoney = totalMoney.add(dto.getPurchaseMoney());
                adviserOrder = new AdviserOrder();
                BeanUtils.copyProperties(dto, adviserOrder);
                adviserOrder.setMainOrderNo(mainOrderNo);
                adviserOrder.setOrderNo(IdGenerator.nextId(orderNo));
                adviserOrder.setOrderState(OrderStateEnum.PAYING.getCode());
                adviserOrder.setPurchaseTime(new Date());
                adviserOrder.setCreateTime(new Date());
                adviserOrders.add(adviserOrder);
            }
            //存入数据库
            if (adviserOrderMapper.insertList(adviserOrders) > 0) {
                log.info("客户或者客户经理(微信端或者pc端)对投顾进行续费或者购买时，mainOrderNo={}，adviserOrders={}", mainOrderNo, JSON.toJSONString(adviserOrders));
                AdviserOrderVO adviserOrderVO = new AdviserOrderVO();
                adviserOrderVO.setMainOrderNo(mainOrderNo);
                adviserOrderVO.setTotalMoney(totalMoney);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, adviserOrderVO);
            } else {
                result.setMessage("投顾订单失败");
            }
        } catch (Exception e) {
            log.error("客户或者客户经理(微信端或者pc端)对投顾进行续费或者购买处理结果异常e={}", e);
        }
        log.info("客户或者客户经理(微信端或者pc端)对投顾进行续费或者购买返回值，result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result addAdviserOrder(AdviserAddOrderDTO adviserAddOrderDTO) {
        log.info("投顾购买入参，adviserAddOrderDTO={}", JSON.toJSONString(adviserAddOrderDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String message = adviserAddOrderDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            String mainOrderNo = "";
            BigDecimal totalMoney = BigDecimal.ZERO;
            AdviserOrder adviserOrder;
            AdviserPurchase adviserPurchase;
            List<AdviserOrder> adviserOrders = new ArrayList<>();
            if (RoleCodeEnum.CUSTOMER.getCode().equals(adviserAddOrderDTO.getPurchaserType())) {
                mainOrderNo = IdGenerator.nextId(OrderBizCode.CUSTOMER_ADVISER_MD);
                //查询投顾购买价格 -- 投顾推荐价格
                for (AdviserRenewDTO adviserRenewDTO : adviserAddOrderDTO.getAdviserRenewDTOS()) {
                    //获取投顾推荐记录信息
                    AdviserRecommend adviserRecommend = new AdviserRecommend();
                    adviserRecommend.setId(adviserRenewDTO.getRecommendId());
                    adviserRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
                    adviserRecommend = adviserRecommendMapper.selectOne(adviserRecommend);
                    if (null == adviserRecommend) {
                        result.setMessage("选择投顾推荐关系不存在");
                        return result;
                    }
                    adviserOrder = new AdviserOrder();
                    //查询购买记录是否存在
                    adviserPurchase = new AdviserPurchase();
                    adviserPurchase.setAdviserId(adviserRenewDTO.getAdviserId());
                    adviserPurchase.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
                    adviserPurchase.setPurchaserId(adviserAddOrderDTO.getPurchaserId());
                    adviserPurchase = adviserPurchaseMapper.selectOne(adviserPurchase);
                    adviserOrder.setOrderType(PurchaseTypeEnum.PURCHASE_BUY_ENUM.getCode());
                    if (null != adviserPurchase) {
                        adviserOrder.setOrderType(PurchaseTypeEnum.PURCHASE_RENEW_ENUM.getCode());
                    }
                    adviserOrder.setRecommendId(adviserRecommend.getId());
                    adviserOrder.setOrderNo(IdGenerator.nextId(OrderBizCode.CUSTOMER_ADVISER_DD));
                    adviserOrder.setOrderState(OrderStateEnum.PAYING.getCode());
                    adviserOrder.setPurchaserId(adviserAddOrderDTO.getPurchaserId());
                    adviserOrder.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
                    adviserOrder.setPurchaseTime(new Date());
                    adviserOrder.setPurchasePrice(adviserRecommend.getPushPrice());
                    adviserOrder.setPurchaseMoney(adviserRecommend.getPushPrice().multiply(new BigDecimal(adviserRenewDTO.getPurchaseMonths())));
                    adviserOrder.setCreateTime(new Date());
                    adviserOrder.setAdviserId(adviserRecommend.getAdviserId());
                    adviserOrder.setMainOrderNo(mainOrderNo);
                    adviserOrder.setManagerId(adviserRecommend.getRecommendedId());
                    adviserOrders.add(adviserOrder);
                    totalMoney = totalMoney.add(adviserOrder.getPurchaseMoney());
                }
            } else if (RoleCodeEnum.MANAGER.getCode().equals(adviserAddOrderDTO.getPurchaserType())) {
                mainOrderNo = IdGenerator.nextId(OrderBizCode.MANAGER_ADVISER_MD);
                //查询投顾购买价格 -- 投顾的分享价格
                for (AdviserRenewDTO adviserRenewDTO : adviserAddOrderDTO.getAdviserRenewDTOS()) {
                    Adviser adviser = adviserMapper.selectByPrimaryKey(adviserRenewDTO.getAdviserId());
                    if (null == adviser) {
                        result.setMessage("选择投顾信息不存在");
                        return result;
                    }
                    if (adviserAddOrderDTO.getPurchaserId().equals(adviser.getManagerId())) {
                        result.setMessage("自己不能购买自己的投顾会");
                        return result;
                    }
                    adviserOrder = new AdviserOrder();
                    //查询购买记录是否存在
                    adviserPurchase = new AdviserPurchase();
                    adviserPurchase.setAdviserId(adviserRenewDTO.getAdviserId());
                    adviserPurchase.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
                    adviserPurchase.setPurchaserId(adviserAddOrderDTO.getPurchaserId());
                    adviserPurchase = adviserPurchaseMapper.selectOne(adviserPurchase);
                    adviserOrder.setOrderType(PurchaseTypeEnum.PURCHASE_BUY_ENUM.getCode());
                    if (null != adviserPurchase) {
                        adviserOrder.setOrderType(PurchaseTypeEnum.PURCHASE_RENEW_ENUM.getCode());
                    }
                    //客户经理购买不存在推荐 -这里存的是创建投顾客户经理的id
                    adviserOrder.setRecommendId(adviser.getManagerId());
                    adviserOrder.setOrderNo(IdGenerator.nextId(OrderBizCode.MANAGER_ADVISER_DD));
                    adviserOrder.setOrderState(OrderStateEnum.PAYING.getCode());
                    adviserOrder.setPurchaserId(adviserAddOrderDTO.getPurchaserId());
                    adviserOrder.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
                    adviserOrder.setPurchaseTime(new Date());
                    adviserOrder.setPurchasePrice(adviser.getPrice());
                    adviserOrder.setPurchaseMoney(adviser.getPrice().multiply(new BigDecimal(adviserRenewDTO.getPurchaseMonths())));
                    adviserOrder.setCreateTime(new Date());
                    adviserOrder.setAdviserId(adviser.getId());
                    adviserOrder.setMainOrderNo(mainOrderNo);
                    adviserOrder.setManagerId(adviser.getManagerId());
                    adviserOrders.add(adviserOrder);
                    totalMoney = totalMoney.add(adviserOrder.getPurchaseMoney());
                }
            }
            //存入数据库
            if (adviserOrderMapper.insertList(adviserOrders) > 0) {
                log.info("投顾购买时，mainOrderNo={}，adviserOrders={}", mainOrderNo, JSON.toJSONString(adviserOrders));
                AdviserOrderVO adviserOrderVO = new AdviserOrderVO();
                adviserOrderVO.setMainOrderNo(mainOrderNo);
                adviserOrderVO.setTotalMoney(totalMoney);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, adviserOrderVO);
            } else {
                result.setMessage("投顾订单失败");
            }
        } catch (Exception e) {
            log.error("投顾购买异常e={}", e);
        }
        log.info("投顾购买返回值，result={}", JSON.toJSONString(result));
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateBuyAdviser(AdviserUpdateOrderDTO adviserUpdateOrderDTO) {
        log.info("修改投顾订单-修改投顾到期时间入参，adviserUpdateOrderDTO={}", JSON.toJSONString(adviserUpdateOrderDTO));
        try {
            //更新订单
            int resultNum = adviserOrderMapper.updateOrderByMainOrderNo(adviserUpdateOrderDTO);
            if (resultNum <= 0) {
                throw new RuntimeException("更新订单异常");
            }
            AdviserOrder adviserOrder = new AdviserOrder();
            adviserOrder.setMainOrderNo(adviserUpdateOrderDTO.getMainOrderNo());
            List<AdviserOrder> list = adviserOrderMapper.select(adviserOrder);
            for (AdviserOrder adviserOrder1 : list) {
                if (PurchaseTypeEnum.PURCHASE_RENEW_ENUM.getCode().equals(adviserOrder1.getOrderType())) {
                    //判断时间
                    AdviserPurchase adviserPurchase = new AdviserPurchase();
                    adviserPurchase.setPurchaserType(adviserOrder1.getPurchaserType());
                    adviserPurchase.setAdviserId(adviserOrder1.getAdviserId());
                    adviserPurchase.setPurchaserId(adviserOrder1.getPurchaserId());
                    adviserPurchase = adviserPurchaseMapper.selectOne(adviserPurchase);
                    Date date = new Date();
                    Date expiryTime;
                    if (adviserPurchase.getExpiryTime().compareTo(date) >= 0) {
                        expiryTime = DateUtil.addMonth(adviserPurchase.getExpiryTime(), 1);
                    } else {
                        expiryTime = DateUtil.addMonth(new Date(), 1);
                    }
                    resultNum = adviserPurchaseMapper.updateAdviserPurchase(CollectionUtils.toMap(
                            "purchaserId", adviserOrder1.getPurchaserId(),
                            "purchaserType", adviserOrder1.getPurchaserType(),
                            "adviserId", adviserOrder1.getAdviserId(),
                            "expiryTime", expiryTime
                    ));
                    if (resultNum <= 0) {
                        throw new RuntimeException("更新投顾到期时间数据异常");
                    }
                } else if (PurchaseTypeEnum.PURCHASE_BUY_ENUM.getCode().equals(adviserOrder1.getOrderType())) {
                    AdviserPurchase adviserPurchase = new AdviserPurchase();
                    adviserPurchase.setCreateTime(new Date());
                    //到期时间
                    adviserPurchase.setExpiryTime(DateUtil.addMonth(new Date(), 1));
                    adviserPurchase.setPurchaserType(adviserOrder1.getPurchaserType());
                    adviserPurchase.setAdviserId(adviserOrder1.getAdviserId());
                    adviserPurchase.setPurchaserId(adviserOrder1.getPurchaserId());
                    adviserPurchase.setManagerId(adviserOrder1.getManagerId());
                    adviserPurchaseMapper.insert(adviserPurchase);
                    if (resultNum <= 0) {
                        throw new RuntimeException("新增投顾到期时间数据异常");
                    }
                }
            }
            Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("修改投顾订单-修改投顾到期时间结果 result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改投顾订单-修改投顾到期时间异常 e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }


    @Override
    public Result getAdviserOrder(String mainOrderNo) {
        log.info("获取投顾订单信息入参，mainOrderNo={}", mainOrderNo);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            AdviserOrder adviserOrder = new AdviserOrder();
            adviserOrder.setMainOrderNo(mainOrderNo);
            List<AdviserOrder> list = adviserOrderMapper.select(adviserOrder);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, list);
        } catch (Exception e) {
            log.error("获取投顾订单信息异常 e={}", e);
        }
        log.info("获取投顾订单信息结果 result={}", JSON.toJSONString(result));
        return result;
    }


    @Override
    public Result getAdviserPush(Integer managerId, PageBean pageBean) {
        log.info("获取客户经理的委托记录入参，managerId={}，pageBean={}", managerId, JSON.toJSONString(pageBean));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
            pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<AdviserPushQueryVO> list = adviserPushMapper.selectPushList(managerId);
            PageInfo<AdviserPushQueryVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取客户经理的委托记录异常e={}", e);
        }
        log.info("获取客户经理的委托记录结果 result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result modifyAdviserPush(Integer pushId) {
        log.info("客户经理的委托记录 撤单入参，pushId={}", pushId);
        String key = "";
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (null == pushId) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                return result;
            }

            AdviserPush adviserPush = adviserPushMapper.selectByPrimaryKey(pushId);
            //撤单时间判断
            if (!checkTime(adviserPush)) {
                result.setMessage("该时间段不能撤单");
                return result;
            }
            //更新记录
            if (!AdviserEnum.TRADE_UN_DEAL.getCode().equals(adviserPush.getTradeStatus())) {
                result.setMessage("委托记录状态已经变更，请刷新页面");
                return result;
            }
            key = AdviserPythonService.getAdviserKey(adviserPush.getAdviserId(), adviserPush.getSharesCode());
            boolean flag = pythonRedisLock.tryLock(key, 10 * 1000L);
            if (!flag) {
                result.setMessage("委托记录正在交易处理，请稍等刷新委托记录");
                return result;
            }
            AdviserTradeRecord adviserTradeRecord = adviserTradeRecordMapper.selectByAdviserId(adviserPush.getAdviserId());
            AdviserPushVO adviserPushVO = new AdviserPushVO();
            BeanUtils.copyProperties(adviserPush, adviserPushVO);
            adviserPushVO.setPushId(adviserPush.getId());
            //缓存更新
            modifyAdviserPush(adviserTradeRecord, adviserPushVO);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("客户经理的委托记录 撤单结果 result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("客户经理的委托记录 撤单异常e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        } finally {
            pythonRedisLock.unLock(key);
        }
    }

    private boolean checkTime(AdviserPush adviserPush) throws ParseException {
        //代码6开头的为上交所，代码为0或3开头的为深交所
        //16：00-次日9：15不可撤单，9：20-9：30不可撤单，（深交所）14：57-15：00不可撤单
        AdviserConfig adviserConfig = new AdviserConfig();
        if (adviserPush.getSharesCode().startsWith(AdviserConstants.SIX)) {
            adviserConfig.setStockType(AdviserEnum.SHANGHAI_STOCK.getCode());
        } else if (adviserPush.getSharesCode().startsWith(AdviserConstants.ZERO) || adviserPush.getSharesCode().startsWith(AdviserConstants.THREE)) {
            adviserConfig.setStockType(AdviserEnum.SHENZHEN_STOCK.getCode());
        }
        adviserConfig.setType(AdviserEnum.CANCEL_TIME.getCode());
        List<AdviserConfig> configList = adviserConfigMapper.select(adviserConfig);
        Date date = new Date();
        String time = DateFormatUtils.format(date, DateUtil.YYYYMMDD);
        StringBuilder stringBuilderStart, stringBuilderEnd;
        if (CollectionUtils.isNotEmpty(configList)) {
            for (AdviserConfig adviserConfig1 : configList) {
                stringBuilderStart = new StringBuilder(time).append(" ");
                stringBuilderEnd = new StringBuilder(time).append(" ");
                stringBuilderStart.append(adviserConfig1.getStartHour()).append(":").append(adviserConfig1.getStartMinute())
                        .append(":00");
                stringBuilderEnd.append(adviserConfig1.getEndHour()).append(":").append(adviserConfig1.getEndMinute())
                        .append(":00");
                if (date.compareTo(DateUtils.parseDate(stringBuilderStart.toString(), DateUtil.YYYYMMDDHHMMSS)) >= 0
                        && date.compareTo(DateUtils.parseDate(stringBuilderEnd.toString(), DateUtil.YYYYMMDDHHMMSS)) <= 0) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public Result modifyAdviserPushScheduled() {
        log.info("委托记录 撤单定时器入参");
        Result result;
        try {
            //交易日判断
            if (!isExecute()) {
                return ResultUtil.setResult(false, "非交易日不更新数据");
            }
            //推送记录-未完成的
            List<AdviserPushVO> list = adviserPushMapper.selectPushAllUnDeal();
            //记录历史
            Map<Integer, AdviserTradeRecord> recordHashMap = new HashMap<>(16);
            AdviserTradeRecord adviserTradeRecord;
            for (AdviserPushVO adviserPushVO : list) {
                try {
                    if (!recordHashMap.containsKey(adviserPushVO.getAdviserId())) {
                        adviserTradeRecord = adviserTradeRecordMapper.selectByAdviserId(adviserPushVO.getAdviserId());
                        modifyAdviserPush(adviserTradeRecord, adviserPushVO);
                        recordHashMap.put(adviserPushVO.getAdviserId(), adviserTradeRecord);
                    } else {
                        modifyAdviserPush(recordHashMap.get(adviserPushVO.getAdviserId()), adviserPushVO);
                    }
                } catch (Exception e) {
                    log.error("客户经理的委托记录 撤单异常", e);
                }
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("客户经理的委托记录 撤单结果 result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("客户经理的委托记录 撤单异常e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result modifyRedisPosition() {
        log.info("每日持仓冻结清零");
        //交易日判断
        if (!isExecute()) {
            return ResultUtil.setResult(false, "非交易日不更新数据");
        }
        adviserPythonService.updatePosition();
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    private boolean isExecute() {
        Object dateTime = pyRedisTemplate.opsForValue().get(CacheKey.TRADE_DAY.getKey());
        log.info("工作日数据，time={}", JSON.toJSONString(dateTime));
        if (null == dateTime) {
            throw new RuntimeException("工作日数据异常");
        }
        List<String> list = JSONArray.parseArray(dateTime.toString(), String.class);
        Date date = new Date();
        String time = DateFormatUtils.format(date, DateUtil.YYYYMMDD);
        if (!list.contains(time)) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteAdviserPosition() {
        log.info("删除持仓数量为0的数据");
        List<AdviserPosition> list = adviserPositionMapper.selectAdviserPositionZero();
        if (CollectionUtils.isNotEmpty(list)) {
            Object[] objects = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                adviserPositionMapper.delete(list.get(i));
                objects[i] = AdviserPythonService.getAdviserKey(list.get(i).getAdviserId(), list.get(i).getSharesCode());
            }
            pyRedisTemplate.opsForHash().delete(CacheKey.ADVISER_POSITION_UPDATE.getKey(), objects);
            pyRedisTemplate.opsForHash().delete(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(), objects);
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result modifyAdviserImportScheduled() {
        log.info("导入定时更新");
        adviserMapper.updateAdviserImport();
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }


    private void modifyAdviserPush(AdviserTradeRecord adviserTradeRecord, AdviserPushVO adviserPushVO) {
        if (null != adviserTradeRecord && DirectionEnum.BUY.getCode().equals(adviserPushVO.getDirection())) {
            //买入需要把可用资金还回去
            updateAdviser(adviserTradeRecord, adviserPushVO);
        } else if (null != adviserTradeRecord && DirectionEnum.SELL.getCode().equals(adviserPushVO.getDirection())) {
            //卖出需要还 持仓数量
            adviserPythonService.updateAdviserSell(adviserTradeRecord, adviserPushVO);
        }

    }

    private void updateAdviser(AdviserTradeRecord adviserTradeRecord, AdviserPushVO adviserPushVO) {
        //买入计算
        if (null != adviserTradeRecord && DirectionEnum.BUY.getCode().equals(adviserPushVO.getDirection())) {
            //计算可用资金(数据库)
            BigDecimal avail = adviserTradeRecord.getAvailableAssets()
                    .add(adviserPushVO.getTradeTotalPrice()
                            .multiply(new BigDecimal(adviserPushVO.getDirection())));
            adviserTradeRecord.setAvailableAssets(avail);
            Object obj = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_TRADE_RECORD.getKey(), AdviserPythonService.getAdviserKey(adviserPushVO.getAdviserId()));
            log.info("获取缓存中的投顾指标记录数据 key={},value={}", adviserPushVO.getAdviserId(), JSON.toJSONString(obj));
            JSONObject jsonObject = JSON.parseObject(obj.toString());
            //计算可用资金(缓存)
            BigDecimal availCache = jsonObject.getBigDecimal(PYTHON_DATA_KEY_ASSETS)
                    .add(adviserPushVO.getTradeTotalPrice()
                            .multiply(new BigDecimal(adviserPushVO.getDirection())));
            //扣除冻结资金
            if (jsonObject.containsKey(PYTHON_DATA_KEY_FREEZE)) {
                BigDecimal totalFreezeNum = jsonObject.getBigDecimal(PYTHON_DATA_KEY_FREEZE)
                        .subtract(adviserPushVO.getTradeTotalPrice()
                                .multiply(new BigDecimal(adviserPushVO.getDirection())));
                jsonObject.put(PYTHON_DATA_KEY_FREEZE, totalFreezeNum);
                if (totalFreezeNum.compareTo(BigDecimal.ZERO) == -1) {
                    throw new RuntimeException("冻结资金异常，totalFreezeNum=" + totalFreezeNum);
                }
            }

            if (avail.compareTo(availCache) == 0) {
                //数据更新
                jsonObject.put(PYTHON_DATA_KEY_ASSETS, availCache);
                adviserPythonService.updateAdviserBuy(adviserTradeRecord, adviserPushVO.getPushId(), jsonObject);
            } else {
                //记录问题
                log.info("计算可用资金(数据库)不一致 ，availCache={},avail={}", availCache, avail);
                throw new RuntimeException("计算可用资金(数据库)不一致");
            }
        }
    }
}
