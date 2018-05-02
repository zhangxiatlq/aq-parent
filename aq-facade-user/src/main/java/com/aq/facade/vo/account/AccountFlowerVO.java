package com.aq.facade.vo.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户经理余额流水VO
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
@Data
@ApiModel(value = "客户账户流水")
public class AccountFlowerVO implements Serializable {

    private static final long serialVersionUID = 3197984168329036466L;

    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private String orderNo;

    @ApiModelProperty(hidden = true)
    private String price;

    @ApiModelProperty(value = "类型描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 流水单号
     */
    @ApiModelProperty(value = "流水单号")
    private String flowNo;

    /**
     * 客户经理余额
     */
    @ApiModelProperty(value = "金额")

    private BigDecimal managerBalance;

    /**
     * 员工余额
     */
    @ApiModelProperty(hidden = true)
    private BigDecimal userBalance;

    /**
     * 客户手机号
     */
    @ApiModelProperty(hidden = true)
    private String customerTelphone;

    /**
     * 客户经理工号
     */
    @ApiModelProperty(hidden = true)
    private Integer idCode;

    /**
     * 员工工号
     */
    @ApiModelProperty(hidden = true)
    private String employeeID;

    /**
     * AQ分成金额
     */
    @ApiModelProperty(hidden = true)
    private BigDecimal aqDivide;

    @ApiModelProperty(hidden = true)
    private String transCode;
}
