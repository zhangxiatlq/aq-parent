package com.aq.facade.vo.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户分页VO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "客户列表VO")
public class QueryCustomerVO implements Serializable {
    private static final long serialVersionUID = 2235223006923625325L;

    @ApiModelProperty(value = "客户id")
    private Integer id;

    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "客户分组名称")
    private String groupName;

    @ApiModelProperty(value = "客户分组Id")
    private Integer groupId;

    @ApiModelProperty(value = "客户姓名")
    private String realName;

    @ApiModelProperty(value = "客户手机号")
    private String telphone;

    @ApiModelProperty(value = "策略推荐数")
    private Integer strategyNum;

    @ApiModelProperty(value = "工具推荐数")
    private Integer toolNum;

    @ApiModelProperty(value = "投顾推荐数")
    private Integer adviserNum;

    @ApiModelProperty(value = "是否绑定微信 1-是 2-否")
    private Byte isBindWebChat;

    @ApiModelProperty(hidden = true)
    private String openId;

    @ApiModelProperty(value = "用户类型 2-客户 3-客户经理")
    private Byte userType;
}
