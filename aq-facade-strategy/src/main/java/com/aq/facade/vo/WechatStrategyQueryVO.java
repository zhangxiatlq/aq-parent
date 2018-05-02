package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信策略列表接口
 *
 * @author 熊克文
 * @createDate 2018\2\12 0012
 **/

@Data
@ApiModel(value = "微信策略列表VO")
public class WechatStrategyQueryVO {

    /**
     * 策略id
     */
    @ApiModelProperty(value = "策略id")
    private Integer strategyId;

    /**
     * 策略购买id
     */
    @ApiModelProperty(value = "策略购买id")
    private Integer purchaseId;

    /**
     * 推荐id
     */
    @ApiModelProperty(value = "推荐id")
    private Integer recommendId;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    /**
     * 策略发布者名称
     */
    @ApiModelProperty(value = "策略发布者名称")
    private String publisherName;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 累计收益(策略从开始那天累积的收益率，我的收益，百分比数值)
     */
    @ApiModelProperty(value = "累计收益")
    private BigDecimal cumulativeIncome;

    /**
     * 年化收益
     */
    @ApiModelProperty(value = "年化收益")
    private BigDecimal annualIncome;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
}
