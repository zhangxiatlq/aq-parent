package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略基准字典VO
 *
 * @author 熊克文
 * @createDate 2018\2\8
 **/
@Data
@ApiModel(value = "策略基准字典")
public class StrategyBenchmarkQueryVO implements Serializable {

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private Integer type;

    /**
     * 基准指数名称
     */
    @ApiModelProperty(value = "基准指数名称")
    private String benchmarkName;

    /**
     * 基准指数值
     */
    @ApiModelProperty(value = "基准指数值")
    private BigDecimal benchmarkNumber;

    /**
     * 来源(用户或其他方式创建)
     */
    @ApiModelProperty(value = "来源")
    private Byte source;

    /**
     * 基准时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "基准时间")
    private Date datumTime;

}
