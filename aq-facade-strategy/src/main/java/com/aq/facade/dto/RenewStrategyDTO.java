package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:20:00 2018-2-12
 * @version:1.0
 * @desc:客户在微信端或者pc端续费或购买策略参数
 */
@ApiModel(value = "客户或者客户经理在微信端或者pc端续费或购买策略参数")
@Data
public class RenewStrategyDTO extends BaseValidate {
    private static final long serialVersionUID = -1353844814681173316L;

    /**
     * 购买记录表id（aq_strategy_purchase：策略购买表）
     */
    private Integer id;
    /**
     * 推荐人id-客户经理id
     */
    @ApiModelProperty(value = "推荐人id-客户经理id", required = true)
    private Integer recommendId;

    /**
     * 购买人id
     */
    @ApiModelProperty(value = "购买人id（客户id或者客户经理id）", required = true)
    private Integer purchaserId;

    /**
     * 购买人类型 2:客户 3 经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @ApiModelProperty(value = "购买人类型 2:客户 3 经理", required = true)
    private Byte purchaserType;

    /**
     * 购买单价
     */
    @ApiModelProperty(value = "购买单价", required = true)
    private BigDecimal purchasePrice;
    /**
     * 购买金额
     */
    @ApiModelProperty(value = "购买金额", required = true)
    private BigDecimal purchaseMoney;
    /**
     * 策略id
     */
    @ApiModelProperty(value = "策略id", required = true)
    private Integer strategyId;

    /**
     * 购买方式
     * {@link com.aq.core.constant.PurchaseTypeEnum}
     */
    @ApiModelProperty(value = "购买方式 1：购买 2：续费", required = true)
    private Byte purchaseType;
}
