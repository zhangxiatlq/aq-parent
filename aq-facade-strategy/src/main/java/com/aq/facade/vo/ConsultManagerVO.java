package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 今日汇客户经理VO
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
@Data
@ApiModel(value = "客户经理-投顾汇VO")
public class ConsultManagerVO implements Serializable{
    private static final long serialVersionUID = -4197011014335015886L;

    @ApiModelProperty(hidden = true)
    private Integer managerId;

    @ApiModelProperty(value = "客户经理头像")
    private String url;

    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称")
    private String adviserName;

    /**
     * 投顾简介
     */
    @ApiModelProperty(value = "投顾简介")
    private String adviserDesc;

    /**
     * 累计收益(策略从开始那天累积的收益率，我的收益，百分比数值)
     */
    @ApiModelProperty(value = "累计收益")
    private String cumulativeIncome;

    /**
     * 年化收益
     */
    @ApiModelProperty(value = "年化收益")
    private String annualIncome;


    @ApiModelProperty(value = "今日汇列表")
    private List<ConsultWeChatVO> list;
}
