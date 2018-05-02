package com.aq.facade.vo.manage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户经理基本信息
 *
 * @author 郑朋
 * @create 2018/2/10 0010
 **/

@Data
@ApiModel(value = "客户经理基本信息")
public class ManagerBaseInfoVO extends ManageInfoVO implements Serializable {
    private static final long serialVersionUID = 4730017804503236917L;

    @ApiModelProperty(value = "银行卡号")
    private String bankcard;

    @ApiModelProperty(value = "身份证号")
    private String cardNo;

    @ApiModelProperty(value = "支付密码")
    private String payPassword;

    @ApiModelProperty(value = "开户地址")
    private String openingAddress;
}
