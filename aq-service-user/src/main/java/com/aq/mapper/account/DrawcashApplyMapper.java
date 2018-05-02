package com.aq.mapper.account;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.account.QueryDrawcashDTO;
import com.aq.facade.entity.account.DrawcashApply;
import com.aq.facade.vo.account.DrawcashAuthDetailVO;
import com.aq.facade.vo.account.DrawcashListVO;

import java.util.List;

/**
 * @author:zhangxia
 * @createTime:15:46 2018-2-23
 * @version:1.0
 * @desc:提现申请 mapper
 */
public interface DrawcashApplyMapper extends BaseMapper<DrawcashApply> {

    /**
     * @Creater: zhangxia
     * @description：后台分页获取提现申请列表
     * @methodName: getDrawcashListByPage
     * @params: [queryDrawcashDTO]
     * @return: java.util.List<com.aq.facade.vo.account.DrawcashListVO>
     * @createTime: 16:32 2018-2-23
     */
    List<DrawcashListVO> getDrawcashListByPage(QueryDrawcashDTO queryDrawcashDTO);

    /**
     * @Creater: zhangxia
     * @description：提现审核结果详情
     * @methodName: getDrawcashAuthDetail
     * @params: [queryDrawcashDTO]
     * @return: com.aq.facade.vo.account.DrawcashAuthDetailVO
     * @createTime: 19:04 2018-2-23
     */
    DrawcashAuthDetailVO getDrawcashAuthDetail(QueryDrawcashDTO queryDrawcashDTO);
}
