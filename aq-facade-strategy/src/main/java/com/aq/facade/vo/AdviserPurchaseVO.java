package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾购买VO
 *
 * @author 郑朋
 * @create 2018/3/2
 */

@Data
@ApiModel(value = "投顾购买")
public class AdviserPurchaseVO implements Serializable {


    private static final long serialVersionUID = -4608913723635934799L;
    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 购买人电话
     */
    @ApiModelProperty(value = "购买人电话")
    private String purchaserTel;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 购买金额
     */
    @ApiModelProperty(value = "购买金额")
    private BigDecimal purchaseMoney;

}