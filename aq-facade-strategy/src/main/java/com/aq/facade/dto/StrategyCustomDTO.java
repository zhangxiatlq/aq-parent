package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:15:30 2018-2-12
 * @version:1.0
 * @desc:客户经理推荐策略给客户时，客户信息dto
 */
@Data
@ApiModel(value = "新增/修改 策略推荐给客户参数")
public class StrategyCustomDTO extends BaseValidate{
    private static final long serialVersionUID = 5615557176754906191L;

    /**
     * 客户经理是否要推荐给客户,
     */
    @ApiModelProperty(value = "客户经理是否要推荐给客户")
    private boolean isPush;
    /**
     *推荐的客户id
     */
    @ApiModelProperty(value = "推荐的客户id",required = true)
    @NotNull(message = "[客户id]不能为空")
    private Integer customerId;
    /**
     * 策略推荐价格
     */
    @ApiModelProperty(value = "策略推荐价格",required = true)
    @NotNull(message = "[推荐价格]不能为空")
    private BigDecimal pushPrice;
}
