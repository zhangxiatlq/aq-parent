package com.aq.mapper.manager;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.manager.Balance;
import com.aq.facade.vo.account.DrawCashVO;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 客户经理余额 mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface BalanceMapper extends BaseMapper<Balance> {

    /**
     * 余额信息--客户经理（仅限）
     *
     * @param managerId
     * @return com.aq.facade.vo.account.DrawCashVO
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    DrawCashVO selectBalanceInfo(Integer managerId);

    /**
     * 减余额
     *
     * @param map
     * @return int
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    int reduceBalance(Map<String, Object> map);

    /**
     * 加余额
     *
     * @param map
     * @return int
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    int increaseBalance(Map<String, Object> map);
}