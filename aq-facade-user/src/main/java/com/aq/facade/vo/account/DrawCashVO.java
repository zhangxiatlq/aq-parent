package com.aq.facade.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提现申请VO
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
@ApiModel(value = "提现申请VO")
public class DrawCashVO implements Serializable {
    private static final long serialVersionUID = -3032978126399930880L;

    @ApiModelProperty(value = "余额")
    private String balance;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "银行卡号")
    private String bankcard;

    @ApiModelProperty(value = "所属银行")
    private String bankName;

    @ApiModelProperty(value = "支付密码",hidden = true)
    private String payPassword;

    @ApiModelProperty(value = "开户地址",hidden = true)
    private String openingAddress;
}
