package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 专用趋势化工具详情DTO
 *
 * @author 郑朋
 * @create 2018/4/26
 **/
@Data
@Api(value = "专用趋势化工具详情DTO")
public class ToolSpecialDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -5556215082858770123L;

//    @ApiModelProperty(value = "股票代码", name = "stockCode")
//    private String stockCode;
//
//    @ApiModelProperty(value = "用户类型", name = "userType")
//    private Byte userType;
//
//    @ApiModelProperty(value = "用户id", name = "userId", required = true)
//    private Integer userId;

    @ApiModelProperty(value = "绑定id", name = "bindId", required = true)
    @NotNull(message = "绑定id不能为空")
    private Integer bindId;

}
