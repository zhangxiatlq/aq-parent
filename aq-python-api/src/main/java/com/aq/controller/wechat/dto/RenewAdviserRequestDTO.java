package com.aq.controller.wechat.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 客户或者客户经理在微信端或者pc端续费或购买投顾会参数
 *
 * @author 郑朋
 * @create 2018/3/7
 */
@ApiModel(value = "客户或者客户经理在微信端或者pc端续费或购买投顾会参数")
@Data
public class RenewAdviserRequestDTO extends BaseValidate implements Serializable{
    private static final long serialVersionUID = 5216318249446184560L;
    
    /**
     * 购买人id
     */
    @ApiModelProperty(value = "购买人id（客户或者客户经理）", hidden = true)
    @NotNull(message = "[购买人]不能为空")
    private Integer purchaserId;
    /**
     * 投顾会id集合和金额
     */
    private List<AdviserDTO> adviserDTOS;
    /**
     * 购买人类型 2 客户  3 客户经理
     *{@link com.aq.core.constant.RoleCodeEnum}
     */
    @NotNull(message = "[购买人类型]不能为空")
    @ApiModelProperty(value = "购买人类型 2 客户  3 客户经理", hidden = true)
    private Byte purchaserType;

    /**
     * 购买方式
     * 购买方式 1：购买 2：续费
     * {@link com.aq.core.constant.PurchaseTypeEnum}
     */
    @ApiModelProperty(value = "购买方式 1：购买 2：续费", required = true)
    @NotNull(message = "[购买方式]不能为空")
    private Byte purchaseType;



}
