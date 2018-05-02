package com.aq.facade.dto.manage;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改客户经理DTO
 *
 * @author 郑朋
 * @create 2018/2/24 0024
 **/
@Data
@ApiModel(value = "修改客户经理头像")
public class UpdateManagerDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -5739751341803046737L;

    @ApiModelProperty(value = "客户经理头像", required = true)
    private String url;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;
}
