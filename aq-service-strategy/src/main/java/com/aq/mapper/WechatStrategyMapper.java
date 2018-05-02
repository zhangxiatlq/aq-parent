package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.StrategyDetailDTO;
import com.aq.facade.dto.WechatStrategyQueryDTO;
import com.aq.facade.entity.Strategy;
import com.aq.facade.vo.StrategyWechatDetailVO;
import com.aq.facade.vo.WechatStrategyQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略mapper
 *
 * @author 熊克文
 * @date 2018-02-08
 */
public interface WechatStrategyMapper extends BaseMapper<Strategy> {

    /**
     * 微信 我的策略
     *
     * @param dto
     * @return {@link com.aq.facade.vo.WechatStrategyQueryVO}
     * @author 熊克文
     */
    List<WechatStrategyQueryVO> listWechatStrategyQueryVO(WechatStrategyQueryDTO dto);

    /**
     * 微信 策略详情
     *
     * @param dto 策略id
     * @return {@link com.aq.facade.vo.WechatStrategyQueryVO}
     * @author 熊克文
     */
    StrategyWechatDetailVO getStrategyWechatDetailVO(StrategyDetailDTO dto);

    /**
     * 微信 策略详情
     *
     * @param strategyId 策略id
     * @return 所有需要返回的openId
     * @author 熊克文
     */
    List<String> selectStrategyPushIds(@Param(value = "strategyId") Integer strategyId);
}