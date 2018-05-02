package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增今日汇DTO
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Data
@ApiModel(value = "新增今日汇")
public class ConsultAddDTO extends BaseValidate implements Serializable {

    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    @Length(max = 15, message = "标题长度不能超过15")
    private String title;

    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空")
    @Length(max = 5000,message = "内容不能超过5000字")
    private String content;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;
}
