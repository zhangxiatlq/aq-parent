package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专用趋势化工具详情VO
 *
 * @author 郑朋
 * @create 2018/4/26
 **/
@Data
@Api(value = "专用趋势化工具详情VO")
public class ToolSpecialVO implements Serializable {
    private static final long serialVersionUID = 9202741418911800145L;

    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 买入日期
     */
    @ApiModelProperty(value = "买入日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date toBuyDate;

    /**
     * 卖出日期
     */
    @ApiModelProperty(value = "卖出日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date toSellDate;

    /**
     * 收益率
     */
    @ApiModelProperty(value = "收益率")
    private Double incomeRate;

    /**
     * 股票代码
     */
    @ApiModelProperty(value = "股票代码")
    private String stockCode;

    /**
     * 买入推送记录id
     */
    @ApiModelProperty(value = "买入推送记录id")
    private Integer buyPushId;

    /**
     * 卖出推送记录id
     */
    @ApiModelProperty(value = "卖出推送记录id")
    private Integer sellPushId;

    /**
     * 用户id（客户或者客户经理表的id）
     */
    @ApiModelProperty(value = "用户id（客户或者客户经理表的id）")
    private Integer userId;

    /**
     * 用户类型（2-客户；3-客户经理）
     *
     * @see com.aq.core.constant.RoleCodeEnum
     */
    @ApiModelProperty(value = "用户类型（2-客户；3-客户经理）")
    private Byte userType;
}
