package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾汇--我购买的列表vo
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "投顾汇--我购买的列表")
public class AdviserListVO implements Serializable{
    private static final long serialVersionUID = 6737322995778953133L;

    @ApiModelProperty(value = "投顾表id")
    private Integer id;

    /**
     * 投顾购买表id
     */
    @ApiModelProperty(value = "投顾购买表id")
    private Integer purchaseId;

    /**
     * 创建投顾人（客户经理）的头像
     */
    @ApiModelProperty(value = "创建投顾人（客户经理）的头像")
    private String publisherPhoto;

    /**
     * 创建投顾人（客户经理）名称
     */
    @ApiModelProperty(value = "创建投顾人（客户经理）名称")
    private String publisherName;

    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称")
    private String adviserName;

    /**
     * 投顾描述
     */
    @ApiModelProperty(value = "投顾描述")
    private String adviserDesc;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 购买价格
     */
    @ApiModelProperty(value = "购买价格")
    private BigDecimal price;

    /**
     * 购买状态(0-已购买;1-未购买)
     * {@link com.aq.facade.contant.PurchaseStateEnum}
     */
    @ApiModelProperty(value = "购买状态(0-已购买;1-未购买)")
    private Byte purchaseState;

    /**
     * 收藏状态 （ 0:收藏,1:取消）
     */
    @ApiModelProperty(value = "收藏状态 （ 0:收藏,1:取消）")
    private Byte collectionState;

    /**
     * 收藏表id
     */
    @ApiModelProperty(value = "收藏表id")
    private Integer collectionId;


    /**
     * 推荐记录id
     */
    @ApiModelProperty(value = "推荐记录id")
    private Integer recommendId;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 投顾推送list
     */
    @ApiModelProperty(value = "投顾推送list")
    private List<AdviserPushQueryVO> adviserPushQueryVOList;

    /**
     * 投顾指标记录List
     */
    @ApiModelProperty(value = "投顾指标记录List")
    private List<AdviserTradeRecordQueryVO> adviserTradeRecordQueryVOList;

    /**
     *客户经理id
     */
    @ApiModelProperty(value = "客户经理id")
    private Integer managerId;
    /**
     * 策略基准指数List
     */
//    @ApiModelProperty(value = "策略基准指数List")
//    private List<StrategyBenchmarkQueryVO> strategyBenchmarkQueryVOList;
}
