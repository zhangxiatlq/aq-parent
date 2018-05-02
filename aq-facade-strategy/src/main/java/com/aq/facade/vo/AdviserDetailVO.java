package com.aq.facade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾详情vo
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
public class AdviserDetailVO  implements Serializable{

    private static final long serialVersionUID = 2974129982305022501L;

    /**
     * 投顾id
     */
    @ApiModelProperty(value = "投顾id")
    private Integer id;

    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称")
    private String adviserName;

    /**
     * 持仓数量
     */
    @ApiModelProperty(value = "持仓数量")
    private Integer positionTotalCount;

    /**
     * 交易数量
     */
    @ApiModelProperty(value = "交易数量")
    private Integer pushTotalCount;

    /**
     * 推荐数量
     */
    @ApiModelProperty(value = "推荐数量")
    private Integer recommendTotalCount;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer purchaseTotalCount;

    /**
     * 投顾指标记录list
     */
    @ApiModelProperty(value = "投顾指标记录list")
    private List<AdviserTradeRecordQueryVO> adviserTradeRecordQueryVOList;

    /**
     * 投顾基准指数list
     */
//    @ApiModelProperty(value = "投顾基准指数list")
//    private List<StrategyBenchmarkQueryVO> strategyBenchmarkQueryVOList;

    /**
     * 投顾持仓list
     */
    @ApiModelProperty(value = "投顾持仓list")
    private List<AdviserPositionVO> adviserPositionVOList;

    /**
     * 投顾推送list
     */
    @ApiModelProperty(value = "投顾推送list")
    private List<AdviserPushQueryVO> adviserPushQueryVOList;

    /**
     * 投顾推荐list
     */
    @ApiModelProperty(value = "投顾推荐list")
    private List<AdviserRecommendVO> adviserRecommendVOList;

    /**
     * 投顾购买记录
     */
    @ApiModelProperty(value = "投顾购买记录")
    private List<AdviserPurchaseVO> adviserPurchaseVOList;
}
