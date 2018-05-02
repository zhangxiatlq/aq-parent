package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author:zhangxia
 * @createTime:11:28 2018-2-12
 * @version:1.0
 * @desc:策略用户列表查询结果VO
 */
@Data
@ApiModel(value = "推荐客户列表")
public class StrategyCustomListVO implements Serializable{
    private static final long serialVersionUID = 2004888045764114150L;

    /**
     * 推荐记录表（aq_strategy_recommend）id
     */
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 客户（客户名称+电话号码后四位）
     */
    @ApiModelProperty(value = "客户")
    private String custom;
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Integer customerId;
    /**
     * 价格（元）
     */
    @ApiModelProperty(value = "推荐价格 月单价")
    private BigDecimal pushPrice;

    /**
     * 是否已经推送过策略给客户
     * {@link com.aq.facade.contant.StrategySentEnum}
     */
    @ApiModelProperty(value = "是否已经推送过策略给客户：0 已推荐；1未推荐")
    private Byte strategySent;
}
