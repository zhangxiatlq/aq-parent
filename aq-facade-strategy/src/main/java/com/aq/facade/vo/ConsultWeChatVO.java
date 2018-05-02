package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 今日汇 VO
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
@Data
@ApiModel(value = "今日汇VO")
public class ConsultWeChatVO implements Serializable {
    private static final long serialVersionUID = 7566663132887688553L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详细内容")
    private String content;

    @ApiModelProperty(value = "发布时间")
    private String createTime;


}
