package com.aq.facade.service.account;

import com.aq.facade.dto.account.QueryDrawcashDTO;
import com.aq.facade.dto.account.UpdateDrawcashDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @author:zhangxia
 * @createTime:17:29 2018-2-23
 * @version:1.0
 * @desc:提现申请service
 */
public interface IAccountApplyService {

    /**
     * @Creater: zhangxia
     * @description：后台分页获取提现申请列表
     * @methodName: getDrawcashListByPage
     * @params: [pageBean, queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 17:32 2018-2-23
     */
    Result getDrawcashListByPage(PageBean pageBean, QueryDrawcashDTO queryDrawcashDTO);

    /**
     * @Creater: zhangxia
     * @description：更新提现申请记录审核
     * @methodName: updateDrawcashApply
     * @params: [updateDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 17:57 2018-2-23
     */
    Result updateDrawcashAuthApply(UpdateDrawcashDTO updateDrawcashDTO);

    /**
     * @Creater: zhangxia
     * @description：提现审核结果详情
     * @methodName: getDrawcashAuthDetail
     * @params: [queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 19:00 2018-2-23
     */
    Result getDrawcashAuthDetail(QueryDrawcashDTO queryDrawcashDTO);
}
