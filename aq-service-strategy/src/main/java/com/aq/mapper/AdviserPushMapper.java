package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.AdviserPush;
import com.aq.facade.vo.AdviserPushQueryVO;
import com.aq.facade.vo.AdviserPushVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投顾推送记录mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserPushMapper extends BaseMapper<AdviserPush> {

    /**
     * 获取推送记录
     *
     * @param managerId
     * @return java.util.List<com.aq.facade.vo.AdviserPushQueryVO>
     * @author 郑朋
     * @create 2018/3/8 0008
     */
    List<AdviserPushQueryVO> selectPushList(@Param("managerId") Integer managerId);

    /**
     * 查询所有未处理的交易
     *
     * @param
     * @return java.util.List<com.aq.facade.vo.AdviserPushVO>
     * @author 郑朋
     * @create 2018/3/8 0008
     */
    List<AdviserPushVO> selectPushAllUnDeal();

    /**
     * 获取投顾推送的openId
     *
     * @param adviserId
     * @return java.util.List<java.lang.String>
     * @author 郑朋
     * @create 2018/3/12
     */
    List<String> selectPushOpenId(@Param(value = "adviserId") Integer adviserId);

}