package com.aq.facade.service;

import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.*;
import com.aq.facade.vo.StrategysSelfSupportQueryVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 策略接口
 *
 * @author 熊克文
 * @createDate 2018\2\8
 **/
public interface IStrategyService {

    /**
     * 精品策略列表查询 PC
     * @author 熊克文
     * @param dto      查询dto
     * @param pageBean 分页参数
     * @return {@link com.aq.facade.vo.BoutiqueStrategyQueryVO}
     */
    Result listStrategyQueryVO(BoutiqueStrategyQueryDTO dto, PageBean pageBean);

    /**
     * 精品策略详情查询 PC
     * @author 熊克文
     * @param dto 查询dto
     * @return {@link com.aq.facade.vo.StrategyDetailVO}
     */
    Result getStrategyDetailVO(StrategyDetailDTO dto);

    /**
     * 策略推送信息分页查询 PC
     *
     * @param strategyId 策略id
     * @param pageBean   分页参数
     * @return {@link com.aq.facade.vo.StrategyPushQueryVO}
     * @author 熊克文
     */
    Result listStrategyPushQueryVO(Integer strategyId, PageBean pageBean);

    /**
     * 策略推荐分页查询 PC
     *
     * @param strategyId 策略id
     * @param managerId  经理id
     * @param pageBean   分页参数
     * @return {@link com.aq.facade.vo.StrategyRecommendVO}
     * @author 熊克文
     */
    Result listStrategyRecommendVO(Integer strategyId, Integer managerId, PageBean pageBean);

    /**
     * 策略购买分页查询 PC
     *
     * @param strategyId 策略id
     * @param managerId  经理id
     * @param pageBean   分页参数
     * @return {@link com.aq.facade.vo.StrategyPurchaseVO}
     * @author 熊克文
     */
    Result listStrategyPurchaseVO(Integer strategyId, Integer managerId, PageBean pageBean);

    /**
     * 策略持仓分页查询
     *
     * @param strategyId 策略id
     * @param pageBean   分页参数
     * @return {@link com.aq.facade.vo.StrategyPositionVO}
     * @author 熊克文
     */
    Result listStrategyPositionVO(Integer strategyId, PageBean pageBean);

    /**
     * 自营策略分页查询
     *
     * @param pageBean 分页条件DTO
     * @param dto      查询条件DTO
     * @return {@link StrategysSelfSupportQueryVO}
     */
    Result pageStrategysSelfSupportQueryVO(PageBean pageBean, StrategysQueryDTO dto);

    /**
     * 策略推送对象分页查询
     *
     * @param pageBean 分页条件DTO
     * @param dto      查询条件DTO
     * @return {@link com.aq.facade.vo.StrategysPushQueryVO}
     */
    Result pageStrategysPushVO(PageBean pageBean, StrategysPushQueryDTO dto);

    /**
     * 策略推送对象分页查询
     * l
     *
     * @param strategysId 策略id
     * @return {@link com.aq.core.constant.RoleCodeEnum}
     */
    Result strategysPushRoleType(Integer strategysId);

    /**
     * 添加策略
     *
     * @param dto 添加DTO
     * @return {@link com.aq.util.result.Result}
     */
    Result addStrategys(StrategysDTO dto);

    /**
     * 查询PC列表单个策略
     *
     * @param id 主键
     * @return {@link com.aq.facade.vo.StrategysVO}
     */
    Result getStrategys(Integer id);

    /**
     * 编辑策略
     *
     * @param dto 编辑策略
     * @return {@link com.aq.util.result.Result}
     */
    Result editStrategys(StrategysDTO dto);

    /**
     * 审核策略
     *
     * @param dto 审核策略dto
     * @return {@link com.aq.util.result.Result}
     */
    Result auditStrategys(StrategysOperateDTO dto);

    /**
     * 删除策略
     *
     * @param id 策略id
     * @return {@link com.aq.util.result.Result}
     */
    Result delStrategys(Integer id);

    /**
     * 推送策略接口
     *
     * @param strategysId   策略id
     * @param roleCodeEnum  {@link com.aq.core.constant.RoleCodeEnum}
     * @param userIds 用户Id数组
     * @param allChecked 是否全选
     * @return Result
     */
    Result pushStrategys(Integer strategysId, RoleCodeEnum roleCodeEnum, String[] userIds, Boolean allChecked);


}
