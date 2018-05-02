package com.aq.facade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 投顾微信详情VO
 *
 * @author 熊克文
 * @createDate 2018\2\13
 **/
@Data
public class AdviserWechatDetailVO implements Serializable {

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

    @ApiModelProperty(value = "投顾简介")
    private String adviserDesc;
    /**
     * 投顾指标记录list
     */
    @ApiModelProperty(value = "投顾指标记录list")
    private List<AdviserTradeRecordQueryVO> adviserTradeRecordQueryVOList;

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
}
