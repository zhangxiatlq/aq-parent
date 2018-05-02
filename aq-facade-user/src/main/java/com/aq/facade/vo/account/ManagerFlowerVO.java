package com.aq.facade.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 客户经理账户流水
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
@ApiModel(value = "客户账户流水")
public class ManagerFlowerVO implements Serializable{

    @ApiModelProperty(value = "流水记录集合")
    private List<AccountFlowerVO> list;

    @ApiModelProperty(value = "余额")
    private String balance;

    /**
     * 累计进账
     */
    @ApiModelProperty(hidden = true)
    private String totalRevenue;

    /**
     * 累计结算
     */
    @ApiModelProperty(hidden = true)
    private String totalSettlement;
}
