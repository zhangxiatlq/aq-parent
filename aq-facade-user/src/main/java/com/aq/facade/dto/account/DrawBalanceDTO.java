package com.aq.facade.dto.account;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 余额提现DTO
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
@ApiModel(value = "余额提现申请")
public class DrawBalanceDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 6725276249373003729L;

    @ApiModelProperty(value = "提现金额", required = true)
    @NotNull(message = "提现金额不能为空")
    @Min(value = 0,message = "提现金额不能小于0元")
    private BigDecimal drawCash;

    @ApiModelProperty(value = "支付密码", required = true)
    @NotBlank(message = "支付密码不能为空")
    private String payPassword;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "客户经理id不能为空")
    private Integer managerId;

    @ApiModelProperty(hidden = true)
    @NotBlank(message = "提现ip地址不能为空")
    private String requestIp;
}
