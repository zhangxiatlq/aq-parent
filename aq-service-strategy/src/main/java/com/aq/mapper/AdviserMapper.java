package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.*;
import com.aq.facade.dto.StrategysQueryDTO;
import com.aq.facade.entity.Adviser;
import com.aq.facade.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 投顾mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserMapper extends BaseMapper<Adviser> {

    /**
     * 查询投顾详情
     *
     * @param managerId
     * @return com.aq.facade.vo.AdviserInfoVO
     * @author 郑朋
     * @create 2018/3/7 0007
     */
    AdviserInfoVO selectAdviserInfo(@Param("managerId") Integer managerId);

    /**
     * 获取投顾列表
     *
     * @param adviserPageDTO
     * @return java.util.List<com.aq.facade.vo.AdviserPageVO>
     * @author 郑朋
     * @create 2018/4/13
     */
    List<AdviserPageVO> selectAdviserList(AdviserPageDTO adviserPageDTO);

    /**
     * 修改投顾
     *
     * @param adviseUpdateDTO
     * @return int
     * @author 郑朋
     * @create 2018/3/7 0007
     */
    int updateAdviserInfo(AdviseUpdateDTO adviseUpdateDTO);

    /**
     * 修改所有的导入字段
     *
     * @param
     * @return int
     * @author 郑朋
     * @create 2018/4/12
     */
    int updateAdviserImport();

    /**
     * @author: zhangxia
     * @desc:客户经理分页查询投顾汇列表接口
     * @params: [dto]
     * @methodName:listAdviserPage
     * @date: 2018/3/7 0007 上午 11:54
     * @return: java.util.List<com.aq.facade.vo.AdviserListVO>
     * @version:2.1.2
     */
    List<AdviserListVO> listAdviserPage(AdviserQueryDTO dto);

    /**
     * @author: zhangxia
     * @desc: 投顾详情查询
     * @params: [dto]
     * @methodName:getAdviserDetailVO
     * @date: 2018/3/7 0007 下午 15:45
     * @return: com.aq.facade.vo.AdviserDetailVO
     * @version:2.1.2
     */
    AdviserDetailVO getAdviserDetailVO(AdviserDetailDTO dto);

    /**
     * 策略推送信息记录
     *
     * @param map strategyId 策略id
     * @return {@link StrategyPushQueryVO}
     * @author 熊克文
     */
    List<AdviserPushQueryVO> listAdviserPushQueryVO(Map<String, Object> map);

    /**
     * 策略推荐记录
     *
     * @param map strategyId 策略id managerId  经理id
     * @return {@link StrategyRecommendVO}
     * @author 熊克文
     */
    List<AdviserRecommendVO> listAdviserRecommendVO(Map<String, Object> map);

    /**
     * 策略购买记录
     *
     * @param map strategyId 策略id managerId  经理id
     * @return {@link StrategyPurchaseVO}
     * @author 熊克文
     */
    List<AdviserPurchaseVO> listAdviserPurchaseVO(Map<String, Object> map);

    /**
     * 策略持仓记录
     *
     * @param map strategyId 策略id
     * @return {@link StrategyPositionVO}
     * @author 熊克文
     */
    List<AdviserPositionVO> listAdviserPositionVO(Map<String, Object> map);

    /**
     * 自营策略查询
     *
     * @param dto 查询条件DTO
     * @return {@link StrategysSelfSupportQueryVO}
     */
    List<StrategysSelfSupportQueryVO> listStrategysSelfSupportQueryVO(StrategysQueryDTO dto);

    /**
     * 投顾推送对象分页查询
     *
     * @param dto 查询条件DTO
     * @return {@link StrategysPushQueryVO}
     */
    List<AdviserPushQueryVO> listAdviserPushVO(StrategysPushQueryDTO dto);

    /**
     * @auth: zhangxia
     * @desc: 实时刷新查询投顾今日收益数据
     * @methodName: listAdviserRefreshVO
     * @params: [dto]
     * @return: java.util.List<com.aq.facade.vo.AdviserListRefreshVO>
     * @createTime: 2018/4/12 14:30
     */
    List<AdviserListRefreshVO> listAdviserRefreshVO(AdviserListRefreshDTO dto);
}