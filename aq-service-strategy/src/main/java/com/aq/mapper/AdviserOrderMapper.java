package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.AdviserUpdateOrderDTO;
import com.aq.facade.entity.AdviserOrder;
import org.springframework.stereotype.Repository;

/**
 * 投顾订单mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserOrderMapper extends BaseMapper<AdviserOrder> {


    /**
     * 通过主订单号更新订单状态
     *
     * @param adviserUpdateOrderDTO
     * @return int
     * @author 郑朋
     * @create 2018/3/7 0007
     */
    int updateOrderByMainOrderNo(AdviserUpdateOrderDTO adviserUpdateOrderDTO);
}