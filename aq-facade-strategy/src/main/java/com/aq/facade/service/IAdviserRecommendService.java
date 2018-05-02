package com.aq.facade.service;

import com.aq.facade.dto.*;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾汇的推荐和购买接口
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
public interface IAdviserRecommendService {

    /**
     * @author: zhangxia
     * @desc:客户经理 分页查询 投顾汇 列表接口
     * @params: [adviserQueryDTO, pageBean]
     * @methodName:getListAdviserPage
     * @date: 2018/3/7 0007 上午 10:43
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result getListAdviserPage(AdviserQueryDTO adviserQueryDTO, PageBean pageBean);

    /**
     * @author: zhangxia
     * @desc: 查询投顾详情
     * @params: [dto]
     * @methodName:getAdviserDetailVO
     * @date: 2018/3/7 0007 下午 16:03
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result getAdviserDetailVO(AdviserDetailDTO dto);

    /**
     * @author: zhangxia
     * @desc: 收藏者收藏投顾汇
     * @params: [dto]
     * @methodName:collectionAdviser
     * @date: 2018/3/7 0007 下午 17:04
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result collectionAdviser(AdviserCollectionDTO dto);


    /**
     * @author: zhangxia
     * @desc: 更新收藏者收藏投顾的数据
     * @params: [dto]
     * @methodName:updateCollectionAdviser
     * @date: 2018/3/7 0007 下午 17:35
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result updateCollectionAdviser(AdviserCollectionUpdateDTO dto);


    /**
     * 获取投顾推荐记录数据
     *
     * @param adviserQueryRecommendDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/8 0008
     */
    Result getRecommendAdviser(AdviserQueryRecommendDTO adviserQueryRecommendDTO);

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾时获取客户列表
     * @params: [dto]
     * @methodName:getAdviserCustomers
     * @date: 2018/3/8 0008 下午 14:21
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result recommendAdviserGetCustomers(AdviserCustomListDTO dto,PageBean pageBean);

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾给客户
     * @params: [dto]
     * @methodName:recommendAdviser
     * @date: 2018/3/8 0008 下午 15:42
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result recommendAdviser(AdviserRecommendDTO dto);

    /**
     * @author: zhangxia
     * @desc: 投顾推荐记录更新微信推送状态
     * @params: [dto]
     * @methodName:updateRecommendAdviserPushstate
     * @date: 2018/3/8 0008 下午 18:16
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result updateRecommendAdviserPushstate(AdviserRecommendUpdateDTO dto);

    /**
     * @author: zhangxia
     * @desc: 查询推荐投顾给客户列表
     * @params: [dto]
     * @methodName:recommendListVO
     * @date: 2018/3/9 0009 下午 14:03
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result recommendAdviserList(AdviserQueryRecommendDTO dto);

    /**
     * @author: zhangxia
     * @desc: 删除客户经理推荐给客户的投顾
     * @params: [updateDTO]
     * @methodName:updateRecommendAdviser
     * @date: 2018/3/9 0009 下午 16:18
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result updateRecommendAdviserDelete(AdviserRecommendUpdateDTO updateDTO);

    /**
     * @auth: zhangxia
     * @desc: 实时刷新查询投顾今日收益数据
     * @methodName: listAdviserRefresh
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/12 14:43
     * @version:2.1.6
     */
    Result listAdviserRefresh(AdviserListRefreshDTO dto);
}
