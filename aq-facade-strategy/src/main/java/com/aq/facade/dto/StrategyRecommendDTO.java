package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author:zhangxia
 * @createTime:15:25 2018-2-12
 * @version:1.0
 * @desc:客户经理推荐策略给客户dto
 */
@Data
@ApiModel(value = "客户经理推荐策略给客户参数")
public class StrategyRecommendDTO extends BaseValidate{
    private static final long serialVersionUID = -6900382731462274881L;

    /**
     * 客户经理id
     */
    @ApiModelProperty(value = "客户经理id",hidden = true)
    @NotNull(message = "[客户经理id]不能为空")
    private Integer managerId;

    /**
     * 推荐的策略id
     */
    @ApiModelProperty(value = "推荐的策略id",required = true)
    @NotNull(message = "[推荐的策略id]不能为空")
    private Integer strategyId;

    /**
     * 被推荐人类型
     */
    @ApiModelProperty(value = "被推荐人类型",hidden = true)
    @NotNull(message = "[被推荐人类型]不能为空")
    private Byte beRecommendedRoleType;

    /**
     * 客户推荐给客户的信息
     */
    private List<StrategyCustomDTO> strategyCustomDTOS;

}
