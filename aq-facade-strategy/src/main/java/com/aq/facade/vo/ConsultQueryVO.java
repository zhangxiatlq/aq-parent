package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 今日汇查询VO
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Data
@ApiModel(value = "今日汇VO")
public class ConsultQueryVO implements Serializable {
    private static final long serialVersionUID = -872255760527249425L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详细内容")
    private String content;

    @ApiModelProperty(value = "是否可见：1、是 2-否")
    private Byte isVisible;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "不可见原因")
    private String reason;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

}
