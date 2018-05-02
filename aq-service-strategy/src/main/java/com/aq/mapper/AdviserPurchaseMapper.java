package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.AdviserPurchaseDTO;
import com.aq.facade.dto.AdviserRenewDTO;
import com.aq.facade.dto.RenewAdviserDTO;
import com.aq.facade.entity.AdviserPurchase;
import com.aq.facade.vo.AdviserPurchaseVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 投顾购买mapper
 *
 * @author 郑朋
 * @create 2018/3/2 
 */
@Repository
public interface AdviserPurchaseMapper extends BaseMapper<AdviserPurchase> {


    /**
     * 查询投顾购买记录
     *
     * @param adviserPurchaseDTO
     * @return java.util.List<com.aq.facade.vo.AdviserPurchaseVO>
     * @author 郑朋
     * @create 2018/3/2
     */
    List<AdviserPurchaseVO> listAdviserPurchaseVO(AdviserPurchaseDTO adviserPurchaseDTO);


    /**
     * 批量更新策略续费记录
     *
     * @param renewAdviserDTOS
     * @return int
     * @author 郑朋
     * @create 2018/3/2
     */
    int updateRenewAdviserPurchase(List<RenewAdviserDTO> renewAdviserDTOS);

    /**
     * 批量更新策略续费记录
     *
     * @param map
     * @return int
     * @author 郑朋
     * @create 2018/3/2
     */
    int updateAdviserPurchase(Map<String,Object> map);

}