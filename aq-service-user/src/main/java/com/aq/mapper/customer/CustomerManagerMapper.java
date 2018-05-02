package com.aq.mapper.customer;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.entity.customer.CustomerManager;
import com.aq.facade.vo.customer.DivideScaleVO;
import com.aq.facade.vo.statistics.CustomerStatisticsPreManagerListVO;
import com.aq.facade.vo.statistics.CustomerStatisticsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户-客户经理 mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface CustomerManagerMapper extends BaseMapper<CustomerManager> {

    /**
     * @Creater: zhangxia
     * @description：统计客户各个指标的数据
     * @methodName: getCustomerStatisticsStable
     * @params: []
     * @return: com.aq.facade.vo.statistics.CustomerStatisticsVO
     * @createTime: 12:30 2018-3-2
     */
    CustomerStatisticsVO getCustomerStatisticsStable();

    /**
     * @Creater: zhangxia
     * @description：统计每个客户下面的客户各项指标
     * @methodName: getCustomerStatisticsOfManager
     * @params: [selectManagerDTO]
     * @return: com.aq.facade.vo.statistics.CustomerStatisticsPreManagerListVO
     * @createTime: 15:45 2018-3-2
     */
    List<CustomerStatisticsPreManagerListVO> getCustomerStatisticsOfManagerList(SelectManagerDTO selectManagerDTO);

    /**
     * @author: zhangxia
     * @desc: 我的用户 整体统计各个指标数量
     * @params: [selectManagerDTO]
     * @methodName:getStatisticsByUser
     * @date: 2018/3/22 0022 下午 16:43
     * @return: com.aq.facade.vo.statistics.CustomerStatisticsVO
     * @version:2.1.2
     */
    CustomerStatisticsVO getStatisticsByUser(SelectManagerDTO selectManagerDTO);
    /**
     * @author: zhangxia
     * @desc: 通过openid比对更新取消关注
     * @params: []
     * @methodName:cancelAttentionUpdate
     * @date: 2018/3/6 0006 下午 13:43
     * @return: int
     * @version:2.1.2
     */
    int cancelAttentionUpdate();

    /**
     * @author: zhangxia
     * @desc: 通过openid比对更新再次关注
     * @params: []
     * @methodName:againAttentionUpdate
     * @date: 2018/3/6 0006 下午 13:43
     * @return: int
     * @version:2.1.2
     */
    int againAttentionUpdate();

    /**
     * 根据客户id 获取平台、客户经理的分成比例
     *
     * @param customerId
     * @return
     * @author 郑朋
     * @create
     */
    DivideScaleVO selectDivideScale(Integer customerId);

}