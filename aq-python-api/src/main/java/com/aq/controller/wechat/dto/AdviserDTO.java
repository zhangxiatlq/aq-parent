package com.aq.controller.wechat.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 投顾会DTO
 *
 * @author 郑朋
 * @create 2018/3/7
 */
@ApiModel(value = "投顾会DTO")
@Data
public class AdviserDTO extends BaseValidate {

    private static final long serialVersionUID = -7852789951407279219L;

    /**
     * 投顾会id
     */
    @ApiModelProperty(value = "购买或者续费投顾会id")
    private Integer id;

    /**
     * 购买月数(默认为1)
     */
    @ApiModelProperty(value = "购买月数")
    private Integer purchaseMonths;

    /**
     * 投顾会购买id
     */
    @ApiModelProperty(value = "投顾会购买id")
    private Integer purchaseId;

    /**
     * 卖方id
     */
    @ApiModelProperty(value = "推荐人id,客户经理购买时-请填写投顾的创建者", required = true)
    @NotNull(message = "[推荐人id]不能为空")
    private Integer recommendId;


}
