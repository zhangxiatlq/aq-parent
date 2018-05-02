package com.aq.facade.service;

import com.aq.facade.dto.*;
import com.aq.facade.entity.Adviser;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

import java.util.List;

/**
 * 投顾 service
 *
 * @author 郑朋
 * @create 2018/3/6
 **/
public interface IAdviserService {

    /**
     * 新增投顾会
     *
     * @param adviserAddDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result addAdviser(AdviserAddDTO adviserAddDTO);

    /**
     * 投顾会详情
     *
     * @param managerId
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.AdviserInfoVO}
     * @author 郑朋
     * @create 2018/3/6
     */
    Result getAdviser(Integer managerId);

    /**
     * 修改投顾会
     *
     * @param adviseUpdateDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result modifyAdviser(AdviseUpdateDTO adviseUpdateDTO);

    /**
     * 删除投顾会
     *
     * @param managerId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result deleteAdviser(Integer managerId);

    /**
     * 获取投顾列表（web）
     *
     * @param adviserPageDTO
     * @param pageBean
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.AdviserPageVO}
     * @author 郑朋
     * @create 2018/3/8
     */
    Result getAdviserList(AdviserPageDTO adviserPageDTO, PageBean pageBean);

    /**
     * 通过投顾id修改投顾信息
     *
     * @param adviserDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/13
     */
    Result modifyAdviserById(AdviserDTO adviserDTO);

    /**
     * 客户或者客户经理(微信端或者pc端)对投顾进行续费或者购买
     *
     * @param renewAdviserDTOS
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result addAdviserOrder(List<RenewAdviserDTO> renewAdviserDTOS);

    /**
     * 投顾购买
     *
     * @param adviserAddOrderDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result addAdviserOrder(AdviserAddOrderDTO adviserAddOrderDTO);


    /**
     * 修改投顾订单-修改投顾到期时间
     *
     * @param adviserUpdateOrderDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/6
     */
    Result updateBuyAdviser(AdviserUpdateOrderDTO adviserUpdateOrderDTO);

    /**
     * 获取投顾订单信息
     *
     * @param mainOrderNo
     * @return com.aq.util.result.Result {@link com.aq.facade.entity.AdviserOrder}
     * @author 郑朋
     * @create 2018/3/6
     */
    Result getAdviserOrder(String mainOrderNo);


    /**
     * 获取客户经理的委托记录
     *
     * @param managerId
     * @param pageBean
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.AdviserPushQueryVO}
     * @author 郑朋
     * @create 2018/3/8
     */
    Result getAdviserPush(Integer managerId, PageBean pageBean);

    /**
     * 客户经理的委托记录 撤单
     *
     * @param pushId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/8
     */
    Result modifyAdviserPush(Integer pushId);

    /**
     * 委托记录 撤单
     *
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/8
     */
    Result modifyAdviserPushScheduled();

    /**
     * 每日持仓冻结清零
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/13
     */
    Result modifyRedisPosition();

    /**
     * 每日持仓清零
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/13
     */
    Result deleteAdviserPosition();

    /**
     * 导入定时更新
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/12
     */
    Result modifyAdviserImportScheduled();
}
