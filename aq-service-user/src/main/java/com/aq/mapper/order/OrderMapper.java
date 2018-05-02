package com.aq.mapper.order;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.order.UpdateOrderDTO;
import com.aq.facade.entity.StrategyOrder;

/**
 * 
 * @ClassName: OrderMapper
 * @Description: 订单mapper
 * @author: lijie
 * @date: 2018年1月29日 下午3:18:15
 */
public interface OrderMapper extends BaseMapper<StrategyOrder>{

    /**
     * @Creater: zhangxia
     * @description：通过主订单号更新订单状态
     * @methodName: updateOrderByMainOrderNo
     * @params: [updateOrderDTO]
     * @return: int
     * @createTime: 14:25 2018-2-13
     */
    int updateOrderByMainOrderNo(UpdateOrderDTO updateOrderDTO);
}
