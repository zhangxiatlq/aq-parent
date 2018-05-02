package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 删除今日汇 DTO
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Data
@ApiModel(value = "删除今日汇DTO")
public class ConsultRemoveDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 9129145237107154519L;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;
}
