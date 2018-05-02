package com.aq.controller.wechat.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author:zhangxia
 * @createTime:16:53 2018-2-10
 * @version:1.0
 * @desc:客户或者客户经理在微信端或者pc端续费或购买策略参数dto
 */
@ApiModel(value = "客户或者客户经理在微信端或者pc端续费或购买策略参数")
@Data
public class RenewStrategyRequstDTO extends BaseValidate{

    private static final long serialVersionUID = -4523110454728277186L;
    /**
     * 购买人id
     */
    @ApiModelProperty(value = "购买人id（客户或者客户经理）", hidden = true)
    @NotNull(message = "[购买人]不能为空")
    private Integer purchaserId;
    /**
     * 策略id集合和金额
     */
    private List<StrategyDTO> strategyIds;
    /**
     * 购买人类型 2 客户  3 客户经理
     *{@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "[购买人类型]不能为空")
    @ApiModelProperty(value = "购买人类型 2 客户  3 客户经理", hidden = true)
    private Byte purchaserType;

    /**
     * 推荐人id
     */
    @ApiModelProperty(value = "推荐人id,客户经理时填充0", required = true)
    @NotNull(message = "[推荐人id]不能为空")
    private Integer recommendId;

}
