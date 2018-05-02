package com.aq.facade.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 投顾会详情 VO
 *
 * @author 郑朋
 * @create 2018/3/6 0006
 **/
@Data
public class AdviserInfoVO implements Serializable {
    private static final long serialVersionUID = 5549027573317813171L;


    /**
     * 分享价格
     */
    @ApiModelProperty(value = "分享价格")
    private String price;

    /**
     * 总资产
     */
    @ApiModelProperty(value = "初始资金")
    private String totalPrice;

    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称")
    private String adviserName;

    /**
     * 是否产生交易 1-是 2-否
     */
    @ApiModelProperty(value = "是否产生交易 1-是 2-否")
    private Byte isTrade;

    /**
     * 是否导入 1-是 2-否
     */
    @ApiModelProperty(value = "是否导入 1-是 2-否")
    private Byte isImport;

    /**
     * 交易数量
     */
    @ApiModelProperty(hidden = true)
    private Integer num;

    /**
     * 投顾简介
     */
    @ApiModelProperty(value = "投顾简介")
    private String adviserDesc;

    /**
     * 客户经理id
     */
    @ApiModelProperty(hidden = true)
    private Integer managerId;

    /**
     * 投顾id
     */
    @ApiModelProperty(value = "投顾id")
    private Integer id;
}
