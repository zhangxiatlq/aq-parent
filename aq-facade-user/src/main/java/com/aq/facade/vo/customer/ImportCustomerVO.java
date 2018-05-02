package com.aq.facade.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 导入客户VO
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@Data
@ApiModel(value = "导入客户返回数据")
public class ImportCustomerVO implements Serializable {
    private static final long serialVersionUID = 7548263300532115526L;

    @ApiModelProperty(value = "导入客户成功数")
    private Integer successNum;


    @ApiModelProperty(value = "导入客户失败数")
    private Integer failureNum;
}
