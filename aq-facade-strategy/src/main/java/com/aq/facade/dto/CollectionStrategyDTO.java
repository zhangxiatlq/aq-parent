package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author:zhangxia
 * @createTime:10:05 2018-2-12
 * @version:1.0
 * @desc:客户经理收藏策略dto
 */
@ApiModel(value = "客户经理收藏策略参数")
@Data
public class CollectionStrategyDTO extends BaseValidate {

    private static final long serialVersionUID = 3623916526826207078L;

    /**
     * 客户经理id
     */
    @ApiModelProperty(value = "客户经理id", hidden = true)
    @NotNull(message = "[客户经理]不能为空")
    private Integer managerId;
    /**
     * 策略id
     */
    @NotNull(message = "[策略]不能为空")
    @ApiModelProperty(value = "策略id", required = true)
    private Integer strategyId;
}
