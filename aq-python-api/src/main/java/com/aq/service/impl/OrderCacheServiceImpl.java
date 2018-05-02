package com.aq.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.OrderStateEnum;
import com.aq.facade.entity.AdviserOrder;
import com.aq.facade.entity.StrategyOrder;
import com.aq.facade.entity.customer.VipOrder;
import com.aq.facade.service.IAdviserService;
import com.aq.facade.service.order.IOrderService;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
import com.aq.util.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: OrderCacheService
 * @Description: 订单缓存获取
 * @author: lijie
 * @date: 2018年2月26日 下午12:36:58
 */
@Slf4j
@Service
public class OrderCacheServiceImpl implements IOrderCacheService {

    @Reference(version = "1.0.0", check = false)
    private IOrderService orderService;

    @Reference(version = "1.0.0", check = false)
    private IAdviserService adviserService;

    @Cacheable(value = "aqOrder", key = "#orderNo")
    @Override
    public OrderCacheInfoVO getCacheOrder(String orderNo) {

        return getOrderCacheInfo(orderNo);
    }

    @CachePut(value = "aqOrder", key = "#info.orderNo")
    @Override
    public OrderCacheInfoVO putCacheOrder(OrderCacheInfoVO info) {
        return info;
    }

    @CacheEvict(value = "aqOrder", key = "#orderNo")
    @Override
    public void evictCacheOrder(String orderNo) {
    }

    /**
     * @return OrderCacheInfoVO
     * @throws
     * @Title: getOrderCacheInfo
     * @Description: 根据订单编号查询订单信息
     * @param: @param orderNo
     * @param: @return
     * @author lijie
     */
    private OrderCacheInfoVO getOrderCacheInfo(String orderNo) {
        OrderCacheInfoVO result = null;
        String bize = IdGenerator.getBizeCode(orderNo);
        if (StringUtils.isNotBlank(bize)) {
            if (OrderBizCode.CUSTOMR_VIP.equals(bize)) {
                VipOrder order = orderService.getVipOrderInfo(orderNo).getData(VipOrder.class);
                log.info("根据订单编号查询订单信息返回数据={}", JSON.toJSONString(order));
                boolean flag = (null != order && !OrderStateEnum.SUCCESS.getCode().equals(order.getStatus()));
                if (flag) {
                    result = new OrderCacheInfoVO();
                    result.setOrderNo(orderNo);
                    result.setPrice(order.getPrice().toString());
                }
            } else if (OrderBizCode.CUSTOMER_ADVISER_MD.equals(bize)
                    || OrderBizCode.MANAGER_ADVISER_MD.equals(bize)) {
                Result orderResult = adviserService.getAdviserOrder(orderNo);
                log.info("根据订单编号查询订单信息返回数据={}", JSON.toJSONString(orderResult));
                if (null != orderResult && orderResult.isSuccess()) {
                    List<AdviserOrder> list = orderResult.getData(List.class);
                    BigDecimal money = BigDecimal.ZERO;
                    for (AdviserOrder adviserOrder : list) {
                        money = money.add(adviserOrder.getPurchaseMoney());
                    }
                    result = new OrderCacheInfoVO();
                    result.setOrderNo(orderNo);
                    result.setPrice(money.toString());
                }
            }else if (OrderBizCode.CUSTOMR_RENEW_MD.equals(bize) ||
                    OrderBizCode.CUSTOMR_BUY_MD.equals(bize)
                    || OrderBizCode.MANAGER_RENEW_MD.equals(bize)
                    || OrderBizCode.MANAGER_BUY_MD.equals(bize)){
                Result orderResult = orderService.getOrdersByMainOrderNo(orderNo);
                log.info("通过主订单号查询数据库中客户购买策略的记录，返回数据={}",JSON.toJSONString(orderResult));
                if (null != orderResult.getData() && orderResult.isSuccess()){
                    List<StrategyOrder> strategyOrders = orderResult.getData(List.class);
                    BigDecimal money = BigDecimal.ZERO;
                    for (StrategyOrder orders:
                            strategyOrders) {
                        money = money.add(orders.getPurchaseMoney());
                    }
                    result = new OrderCacheInfoVO();
                    result.setOrderNo(orderNo);
                    result.setPrice(money.toString());
                }
            }
        }
        return result;
    }
}
