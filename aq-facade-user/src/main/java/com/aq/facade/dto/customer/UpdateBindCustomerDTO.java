package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改客户DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "修改客户")
public class UpdateBindCustomerDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 9028293981659813039L;

    @ApiModelProperty(value = "新客户分组id", required = true)
    @NotNull(message = "新客户分组id不能为空")
    private Integer newGroupId;

    @ApiModelProperty(value = "老客户分组id", required = true)
    @NotNull(message = "老客户分组id不能为空")
    private Integer oldGroupId;

    @ApiModelProperty(value = "客户经理id", hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;

    @ApiModelProperty(value = "客户id",required = true)
    @NotNull(message = "客户id不能为空")
    private Integer customerId;
}
