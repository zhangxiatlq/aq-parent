package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.facade.dto.StrategyPurchaseDTO;
import com.aq.facade.entity.StrategyPurchase;
import com.aq.facade.vo.StrategyPurchaseVO;

import java.util.List;

/**
 * 策略购买mapper
 *
 * @author 熊克文
 * @date 2018-02-08
 */
public interface StrategyPurchaseMapper extends BaseMapper<StrategyPurchase> {

    /**
     * 查询策略购买记录
     *
     * @param dto 查询条件
     * @return {@link StrategyPurchaseVO}
     */
    List<StrategyPurchaseVO> listStrategyPurchaseVO(StrategyPurchaseDTO dto);

    /**
     * @Creater: zhangxia
     * @description：批量更新策略续费记录
     * @methodName: updateRenewStrategyPurchase
     * @params: [renewStrategyDTOS]
     * @return: int
     * @createTime: 13:35 2018-2-22
     */
    int updateRenewStrategyPurchase(List<RenewStrategyDTO> renewStrategyDTOS);

}