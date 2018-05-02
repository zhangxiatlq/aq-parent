package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime: 2018/4/12 14:25
 * @version:1.0
 * @desc:投顾列表数据实时刷新VO
 */
@Data
@ApiModel(value = "投顾列表数据实时刷新VO")
public class AdviserListRefreshVO implements Serializable{

    private static final long serialVersionUID = -3298824509264915225L;

    /**
     * 投顾id
     */
    @ApiModelProperty(value = "投顾id")
    private Integer adviserId;
    /**
     * 今日收益率
     */
    @ApiModelProperty(value = "今日收益率")
    private BigDecimal todayRate;
}
