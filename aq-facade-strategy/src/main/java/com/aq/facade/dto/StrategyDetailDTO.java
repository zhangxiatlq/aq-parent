package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 策略详情查询DTO
 *
 * @author 熊克文
 * @createDate 2018\2\9 0009
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class StrategyDetailDTO extends BaseValidate {

    /**
     * 策略id
     */
    @NotNull(message = "策略id不能为空")
    private Integer strategyId;


    /**
     * 购买人类型 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "购买人类型不存在")
    private Byte purchaserType;

    /**
     * 购买人id
     */
    @NotNull(message = "购买人id不能为空")
    private Integer purchaserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否需要限制数据条数
     */
    private String isLimit;

}
