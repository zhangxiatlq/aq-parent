package com.aq.facade.service.statistics;

import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @author:zhangxia
 * @createTime:11:19 2018-3-2
 * @version:1.0
 * @desc:后台客户统计数据service
 */
public interface IUserStatisticsService {

    /**
     * @Creater: zhangxia
     * @description：统计客户各个指标的数据
     * @methodName: getUserStaticsStable
     * @params: []
     * @return: com.aq.util.result.Result
     * @createTime: 11:23 2018-3-2
     */
    Result getCustomerStatisticsStable();

    /**
     * @Creater: zhangxia
     * @description：统计客户经理下的客户用户各个指标数据
     * @methodName: getManagerStatisticsList
     * @params: [pageBean,selectManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 11:27 2018-3-2
     */
    Result getManagerStatisticsList(PageBean pageBean,SelectManagerDTO selectManagerDTO);

    /**
     * @author: zhangxia
     * @desc:我的用户 整体统计各个指标数量
     * @params: [dto]
     * @methodName:getCustomerStatisticsStableByUser
     * @date: 2018/3/22 0022 下午 16:51
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result getCustomerStatisticsStableByUser(SelectManagerDTO dto);
}
