package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.StrategyCustomListDTO;
import com.aq.facade.entity.StrategyRecommend;
import com.aq.facade.vo.StrategyCustomListVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略推荐记录mapper
 *
 * @author 熊克文
 * @date 2018-02-08
 */
public interface StrategyRecommendMapper extends BaseMapper<StrategyRecommend> {

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略时获取客户信息列表
     * @methodName: getStrategyCustomers
     * @params: [strategyCustomListDTO]
     * @return: java.util.List<com.aq.facade.vo.StrategyCustomListVO>
     * @createTime: 15:00 2018-2-12
     */
    List<StrategyCustomListVO> getStrategyCustomers(StrategyCustomListDTO strategyCustomListDTO);

    /**
     * @Creater: zhangxia
     * @description：批量更新推荐策略给客户
     * @methodName: updateStrategyRecommendBatch
     * @params: [list]
     * @return: int
     * @createTime: 16:52 2018-2-12
     */
    int updateStrategyRecommendBatch(List<StrategyRecommend> list);

    /**
     * 添加推荐记录
     *
     * @param roleCode    角色code
     * @param strategysId 策略id
     * @param price       策略价格
     * @author: 熊克文
     */
    void insertStrategyRecommend(@Param(value = "roleCode") Byte roleCode, @Param(value = "strategysId") Integer strategysId, @Param(value = "price") BigDecimal price);
}