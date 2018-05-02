package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.ToolSpecialDTO;
import com.aq.facade.entity.ToolIncomeRate;
import com.aq.facade.vo.ToolSpecialVO;
import com.aq.service.impl.ToolServiceImpl;

import java.util.List;

/**
 * @author:zhangxia
 * @createTime: 2018/4/26 18:09
 * @version:1.0
 * @desc:工具收益率表
 */
public interface ToolIncomeRateMapper extends BaseMapper<ToolIncomeRate> {

    /**
     * 专用趋势化工具详情
     *
     * @param toolSpecialDTO
     * @return java.util.List<com.aq.facade.vo.ToolSpecialVO>
     * @author 郑朋
     * @create 2018/4/26
     */
    List<ToolSpecialVO> selectToolSpecial(ToolSpecialDTO toolSpecialDTO);
}
