package com.aq.facade.service;

import com.aq.facade.dto.*;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @author:zhangxia
 * @createTime:9:59 2018-2-12
 * @version:1.0
 * @desc:客户推荐策略service接口
 */
public interface IStrategyRecommendService {

    /**
     * @Creater: zhangxia
     * @description：客户经理收藏策略
     * @methodName: collectionStrategy
     * @params: [collectionStrategyDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:09 2018-2-12
     */
    Result collectionStrategy(CollectionStrategyDTO collectionStrategyDTO);

    /**
     * @Creater: zhangxia
     * @description：更新客户经理收藏策略
     * @methodName: updateCollectionStrategy
     * @params: [collectionStrategyUpdateDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:46 2018-2-12
     */
    Result updateCollectionStrategy(CollectionStrategyUpdateDTO collectionStrategyUpdateDTO);

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略时获取客户信息列表
     * @methodName: getStrategyCustomers
     * @params: [listDTO,pageBean]
     * @return: com.aq.util.result.Result
     * @createTime: 14:54 2018-2-12
     */
    Result getStrategyCustomers(StrategyCustomListDTO listDTO,PageBean pageBean);

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略给客户
     * @methodName: recommendStrategy
     * @params: [strategyRecommendDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 15:47 2018-2-12
     */
    Result recommendStrategy(StrategyRecommendDTO strategyRecommendDTO);

    /**
     * @Creater: zhangxia
     * @description：获取策略推荐记录数据
     * @methodName: getRecommendStrategy
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 16:56 2018-2-26
     */
    Result getRecommendStrategy(QueryStrategyRecommendDTO dto);
}