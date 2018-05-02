package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：微信 投顾列表接口
 * @author： 张霞
 * @createTime： 2018/03/12
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "微信投顾列表VO")
public class WechatAdviserQueryVO implements Serializable{
    private static final long serialVersionUID = -2084510719658594195L;

    /**
     * 投顾id
     */
    @ApiModelProperty(value = "投顾id")
    private Integer adviserId;

    /**
     * 投顾购买表id
     */
    @ApiModelProperty(value = "投顾购买表id")
    private Integer purchaseId;

    /**
     * 推荐表id
     */
    @ApiModelProperty(value = "推荐表id")
    private Integer recommendId;

    /**
     * 投顾名称
     */
    @ApiModelProperty(value = "投顾名称")
    private String adviserName;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryTime;

    /**
     * 累计收益(投顾从开始那天累积的收益率，我的收益，百分比数值)
     */
    @ApiModelProperty(value = "累计收益")
    private BigDecimal cumulativeIncome;

    /**
     * 年化收益
     */
    @ApiModelProperty(value = "年化收益")
    private BigDecimal annualIncome;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     *客户经理头像
     */
    @ApiModelProperty(value = "客户经理头像")
    private String url;

    /**
     * 投顾简介
     */
    @ApiModelProperty(value = "投顾简介")
    private String adviserDesc;
}
