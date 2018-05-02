package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 精品策略列表VO
 *
 * @author 熊克文
 * @createDate 2018\2\8
 **/
@Data
@ApiModel(value = "精品策略")
public class BoutiqueStrategyQueryVO {

    /**
     * 策略id
     */
    @ApiModelProperty(value = "策略id")
    private Integer id;

    /**
     * 策略购买id
     */
    @ApiModelProperty(value = "策略购买id")
    private Integer purchaseId;

    /**
     * 发布者头像
     */
    @ApiModelProperty(value = "发布者头像")
    private String publisherPhoto;

    /**
     * 发布者名称
     */
    @ApiModelProperty(value = "发布者名称")
    private String publisherName;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    /**
     * 策略描述
     */
    @ApiModelProperty(value = "策略描述")
    private String strategyDesc;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     * 购买状态
     */
    @ApiModelProperty(value = "购买状态")
    private Boolean purchaseState;

    /**
     * 收藏状态 （ 0:收藏,1:取消）
     */
    @ApiModelProperty(value = "收藏状态 （ 0:收藏,1:取消）")
    private Byte collectionState;

    /**
     * 收藏id
     */
    @ApiModelProperty(value = "收藏id")
    private Integer collectionId;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 策略推送list
     */
    @ApiModelProperty(value = "策略推送list")
    private List<StrategyPushQueryVO> strategyPushQueryVOList;

    /**
     * 策略交易记录List
     */
    @ApiModelProperty(value = "策略交易记录List")
    private List<StrategyTradeRecordQueryVO> strategyTradeRecordQueryVOList;

    /**
     * 策略基准指数List
     */
    @ApiModelProperty(value = "策略基准指数List")
    private List<StrategyBenchmarkQueryVO> strategyBenchmarkQueryVOList;
}
