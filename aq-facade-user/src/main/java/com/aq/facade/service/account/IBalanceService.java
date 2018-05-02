package com.aq.facade.service.account;

import com.aq.facade.dto.account.UpdateBalanceDTO;
import com.aq.util.result.Result;

/**
 * 余额service
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
public interface IBalanceService {

    /**
     * 修改余额（增加流水）
     *
     * @param updateBalanceDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    Result modifyBalance(UpdateBalanceDTO updateBalanceDTO);

    /**
     * 修改余额（无流水）
     *
     * @param updateBalanceDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    Result modifyBalanceNoFlow(UpdateBalanceDTO updateBalanceDTO);

    /**
     * 获取余额信息
     *
     * @param accountId
     * @param roleType
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/23
     */
    Result getBalance(Integer accountId, Byte roleType);
}
