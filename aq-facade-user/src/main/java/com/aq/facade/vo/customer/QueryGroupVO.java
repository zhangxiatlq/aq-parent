package com.aq.facade.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询分组VO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "查询分组返回信息")
public class QueryGroupVO implements Serializable {
    private static final long serialVersionUID = 1392556206150326652L;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "分组id")
    private Integer id;

    @ApiModelProperty(value = "客户数量")
    private Integer customerNum;

    @ApiModelProperty(value = "是否默认 1-是 2-否")
    private Byte isDefault;
}
