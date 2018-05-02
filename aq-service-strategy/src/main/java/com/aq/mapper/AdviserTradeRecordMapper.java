package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.AdviserTradeRecord;
import com.aq.facade.vo.AdviserTradeVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 投顾交易记录mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserTradeRecordMapper extends BaseMapper<AdviserTradeRecord> {

    /**
     * 根据投顾id 查询最新一条交易记录
     *
     * @param adviserId
     * @return com.aq.facade.entity.AdviserTradeRecord
     * @author 郑朋
     * @create 2018/3/9 0009
     */
    AdviserTradeRecord selectByAdviserId(@Param("adviserId") Integer adviserId);
    /**
     * 
    * @Title: selectAdviserTradeInfo  
    * @Description: 根据投顾id查询资产信息 
    * @param: @param adviserId
    * @param: @return
    * @return AdviserTradeVO
    * @author lijie
    * @throws
     */
	AdviserTradeVO selectAdviserTradeInfo(Integer adviserId);
}