package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 绑定客户DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "新增客户（绑定客户）")
public class BindCustomerDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -9063597517764045116L;

    @ApiModelProperty(value = "客户手机号", required = true)
    @NotBlank(message = "客户手机号不能为空")
    private String telphone;

    @ApiModelProperty(value = "客户姓名", required = true)
    @NotBlank(message = "客户姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "客户分组id", required = true)
    @NotNull(message = "客户分组id不能为空")
    private Integer groupId;

    @ApiModelProperty(value = "客户经理id", hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;
}
