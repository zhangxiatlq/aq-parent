package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.AdviserCustomListDTO;
import com.aq.facade.dto.AdviserQueryRecommendDTO;
import com.aq.facade.entity.AdviserRecommend;
import com.aq.facade.vo.AdviserCustomListVO;
import com.aq.facade.vo.AdviserRecommendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投顾推荐记录mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserRecommendMapper extends BaseMapper<AdviserRecommend> {

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾时获取客户信息列表
     * @params: [adviserCustomListDTO]
     * @methodName:getAdviserCustomers
     * @date: 2018/3/8 0008 下午 14:29
     * @return: java.util.List<com.aq.facade.vo.StrategyCustomListVO>
     * @version:2.1.2
     */
    List<AdviserCustomListVO> getAdviserCustomers(AdviserCustomListDTO adviserCustomListDTO);

    /**
     * @Creater: zhangxia
     * @description：批量更新推荐策略给客户
     * @methodName: updateStrategyRecommendBatch
     * @params: [list]
     * @return: int
     * @createTime: 16:52 2018-2-12
     */
    int updateAdviserRecommendBatch(List<AdviserRecommend> list);

    /**
     * @author: zhangxia
     * @desc:查询推荐投顾给客户列表
     * @params: [dto]
     * @methodName:recommendAdviserList
     * @date: 2018/3/9 0009 下午 14:09
     * @return: java.util.List<com.aq.facade.vo.AdviserRecommendVO>
     * @version:2.1.2
     */
    List<AdviserRecommendVO> recommendAdviserList(AdviserQueryRecommendDTO dto);

    /**
     * 通过投顾id 查询是否推送自己
     *
     * @param adviserId
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/16
     */
    List<String> selectManagerOpenId(@Param(value = "adviserId") Integer adviserId);
}