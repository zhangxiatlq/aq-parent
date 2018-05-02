package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略推荐VO
 *
 * @author 熊克文
 * @date 2018-02-08
 */
@Data
@ApiModel(value = "策略推荐")
public class StrategyRecommendVO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 推荐时间
     */
    @ApiModelProperty(value = "推荐时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date pushDate;

    /**
     * 被推荐客户姓名
     */
    @ApiModelProperty(value = "被推荐客户姓名")
    private String beRecommendedName;

    /**
     * 推荐价格  月单价
     */
    @ApiModelProperty(value = "推荐价格  月单价")
    private BigDecimal pushPrice;

}