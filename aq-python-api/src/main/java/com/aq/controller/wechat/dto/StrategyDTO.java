package com.aq.controller.wechat.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:zhangxia
 * @createTime:15:34 2018-2-10
 * @version:1.0
 * @desc:购买或者续费策略dto
 */
@ApiModel(value = "购买或者续费策略dto")
@Data
public class StrategyDTO extends BaseValidate{

    private static final long serialVersionUID = -7852789951407279219L;

    /**
     * 策略id
     */
    @ApiModelProperty(value = "购买或者续费策略id")
    private Integer id;
    /**
     *购买月数(默认为1)
     */
    @ApiModelProperty(value = "购买月数")
    private Integer purchaseMonths;

    /**
     * 策略购买id
     */
    @ApiModelProperty(value = "策略购买id")
    private Integer purchaseId;




}
