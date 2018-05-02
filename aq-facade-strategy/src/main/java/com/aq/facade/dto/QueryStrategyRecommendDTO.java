package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

/**
 * @author:zhangxia
 * @createTime:16:25 2018-2-26
 * @version:1.0
 * @desc: 查询被推荐人的策略推荐dto
 */
@Data
public class QueryStrategyRecommendDTO extends BaseValidate{
    private static final long serialVersionUID = 7635544879327543765L;

    /**
     * 策略id
     */
    private Integer strategyId;
    /**
     * 推荐人id 0为平台推荐 其余则是客户经理id
     */
    private Integer recommendedId;

    /**
     * 被推荐人类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte beRecommendedRoleType;

    /**
     * 被推荐id(如果推荐id为0 是客户经理id 其余是 客户id)
     */
    private Integer beRecommendedId;
}
