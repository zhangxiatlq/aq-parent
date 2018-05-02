package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增修改DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "新增/修改参数")
public class AddOrUpdateGroupDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -9101427847822907249L;

    @NotBlank(message = "分组名称不能为空")
    @ApiModelProperty(value = "分组名称", required = true)
    private String groupName;

    @NotNull(message = "客户经理id不能为空")
    @ApiModelProperty(hidden = true)
    private Integer managerId;

    @ApiModelProperty(value = "分组id[修改]")
    private Integer id;
}
