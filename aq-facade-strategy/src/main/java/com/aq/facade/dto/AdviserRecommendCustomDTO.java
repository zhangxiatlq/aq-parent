package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户经理推荐投顾给客户时，客户信息dto
 * @author： 张霞
 * @createTime： 2018/03/08
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "新增/修改 客户经理推荐投顾给客户参数")
public class AdviserRecommendCustomDTO extends BaseValidate{

    private static final long serialVersionUID = -4035815385942810862L;
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
     * 投顾推荐价格
     */
    @ApiModelProperty(value = "投顾推荐价格",required = true)
    @NotNull(message = "[推荐价格]不能为空")
    private BigDecimal pushPrice;

    /**
     * 被推荐人类型
     */
    @ApiModelProperty(value = "被推荐人类型",hidden = true)
    @NotNull(message = "[被推荐人类型]不能为空")
    private Byte beRecommendedRoleType;
}
