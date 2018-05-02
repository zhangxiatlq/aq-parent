package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改投顾会 DTO
 *
 * @author 郑朋
 * @create 2018/3/7
 **/
@Data
public class AdviseUpdateDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 6307151709872273206L;

    /**
     * 分享价格
     */
    @ApiModelProperty(value = "分享价格", required = true)
    @NotNull(message = "分享价格不能为空")
    @Min(value = 0, message = "分享价格不能小于0")
    private BigDecimal price;


    /**
     * 客户经理ID
     */
    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理ID不能为空")
    private Integer managerId;

    /**
     * 总资产
     */
    @ApiModelProperty(value = "初始资金")
    private BigDecimal totalPrice;

    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称", required = true)
    @NotBlank(message = "投顾名称不能为空")
    @Length(max = 10,message = "投顾名称不超过10个字")
    private String adviserName;

    /**
     * 投顾简介
     */
    @ApiModelProperty(value = "模拟盘简介", required = true)
    @NotBlank(message = "模拟盘简介不能为空")
    @Length(max = 20,message = "模拟盘简介不超过20个字")
    private String adviserDesc;
}
