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
 * @describe：投顾推荐的客户列表vo
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value = "投顾推荐的客户列表vo")
public class AdviserRecommendVO implements Serializable{

    private static final long serialVersionUID = -1839936234223405410L;

    /**
     * 推荐表id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 推荐时间
     */
    @ApiModelProperty(value = "推荐时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date pushDate;

    /**
     * 被推荐客户姓名
     */
    @ApiModelProperty(value = "被推荐客户姓名")
    private String beRecommendedName;

    /**
     * 推荐价格  月单价
     */
    @ApiModelProperty(value = "推荐价格  月单价")
    private BigDecimal pushPrice;
}
