package com.aq.facade.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询列表今日汇DTO
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Data
public class ConsultQueryDTO implements Serializable {
    private static final long serialVersionUID = 5737130802810365259L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "是否可见：1、是 2-否")
    private Byte isVisible;

    @ApiModelProperty(hidden = true)
    private Integer managerId;

    @ApiModelProperty(value = "排序 asc：顺序 desc：倒序")
    private String orderType;
}
