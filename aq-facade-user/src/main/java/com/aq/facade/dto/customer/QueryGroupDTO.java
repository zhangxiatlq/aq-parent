package com.aq.facade.dto.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分组查询DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "分组查询参数")
public class QueryGroupDTO implements Serializable {
    private static final long serialVersionUID = 6741983225921869762L;

    /**
     * 客户经理id
     */
    @ApiModelProperty(hidden = true)
    private Integer managerId;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
}
