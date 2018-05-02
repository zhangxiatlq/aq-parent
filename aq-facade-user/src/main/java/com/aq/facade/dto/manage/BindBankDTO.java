package com.aq.facade.dto.manage;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 绑定银行卡
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@ApiModel(value = "绑定银行卡参数")
@Data
public class BindBankDTO extends BaseValidate implements Serializable {

    private static final long serialVersionUID = -4457366392203820679L;

    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankcard;
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @ApiModelProperty(value = "身份证号", required = true)
    private String cardNo;
    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

    /**
     * 银行卡开户地址
     */
    @NotBlank(message = "银行卡开户地址不能为空")
    @ApiModelProperty(value = "银行卡开户地址", required = true)
    private String openingAddress;

    /**
     * 客户经理id
     */
    @NotNull(message = "客户经理id不能为空")
    @ApiModelProperty(hidden = true)
    private Integer managerId;

    /**
     * ip
     */
    @NotBlank(message = "ip不能为空")
    @ApiModelProperty(hidden = true)
    private String createIp;
}
