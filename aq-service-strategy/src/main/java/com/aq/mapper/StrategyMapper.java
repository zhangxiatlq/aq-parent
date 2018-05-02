package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.BoutiqueStrategyQueryDTO;
import com.aq.facade.dto.StrategyDetailDTO;
import com.aq.facade.dto.StrategysPushQueryDTO;
import com.aq.facade.dto.StrategysQueryDTO;
import com.aq.facade.entity.Strategy;
import com.aq.facade.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 策略mapper
 *
 * @author 熊克文
 * @date 2018-02-08
 */
public interface StrategyMapper extends BaseMapper<Strategy> {

    /**
     * 精品策略列表查询
     * @author 熊克文
     * @param dto 查询dto
     * @return {@link com.aq.facade.vo.BoutiqueStrategyQueryVO}
     */
    List<BoutiqueStrategyQueryVO> listStrategyQueryVO(BoutiqueStrategyQueryDTO dto);

    /**
     * 精品策略详情查询
     * @author 熊克文
     * @param dto 查询dto
     * @return {@link com.aq.facade.vo.StrategyDetailVO}
     */
    StrategyDetailVO getStrategyDetailVO(StrategyDetailDTO dto);

    /**
     * 策略推送信息记录
     *
     * @param map strategyId 策略id
     * @return {@link com.aq.facade.vo.StrategyPushQueryVO}
     * @author 熊克文
     */
    List<StrategyPushQueryVO> listStrategyPushQueryVO(Map<String, Object> map);

    /**
     * 策略推荐记录
     *
     * @param map strategyId 策略id managerId  经理id
     * @return {@link com.aq.facade.vo.StrategyRecommendVO}
     * @author 熊克文
     */
    List<StrategyRecommendVO> listStrategyRecommendVO(Map<String, Object> map);

    /**
     * 策略购买记录
     *
     * @param map strategyId 策略id managerId  经理id
     * @return {@link com.aq.facade.vo.StrategyPurchaseVO}
     * @author 熊克文
     */
    List<StrategyPurchaseVO> listStrategyPurchaseVO(Map<String, Object> map);

    /**
     * 策略持仓记录
     *
     * @param map strategyId 策略id
     * @return {@link com.aq.facade.vo.StrategyPositionVO}
     * @author 熊克文
     */
    List<StrategyPositionVO> listStrategyPositionVO(Map<String, Object> map);

    /**
     * 自营策略查询
     *
     * @param dto 查询条件DTO
     * @return {@link StrategysSelfSupportQueryVO}
     */
    List<StrategysSelfSupportQueryVO> listStrategysSelfSupportQueryVO(StrategysQueryDTO dto);

    /**
     * 策略推送对象分页查询
     *
     * @param dto 查询条件DTO
     * @return {@link com.aq.facade.vo.StrategysPushQueryVO}
     */
    List<StrategysPushQueryVO> listStrategysPushVO(StrategysPushQueryDTO dto);
}