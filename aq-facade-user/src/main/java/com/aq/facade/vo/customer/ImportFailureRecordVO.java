package com.aq.facade.vo.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 导入客户失败记录表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@ApiModel(value = "导入客户失败列表")
public class ImportFailureRecordVO implements Serializable {
    private static final long serialVersionUID = 647147359492111676L;
    
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telphone;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realName;

    /**
     * 客户分组名称（默认：我的好友）
     */
    @ApiModelProperty(value = "客户分组名称")
    private String groupName;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

}