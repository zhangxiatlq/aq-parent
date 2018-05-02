package com.aq.controller.wechat.customer.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: WeChatCustomerInfoVO
 * @Description: 客户信息数据
 * @author: lijie
 * @date: 2018年2月8日 下午4:42:00
 */
@Data
@ApiModel(value="客户信息数据")
public class ApiCustomerInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8231412833834696612L;

	/**
	 * 客户ID
	 */
	@ApiModelProperty(value="客户ID")
	private Integer id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value="用户名")
    private String username;
    /**
     * 手机号
     */
	@ApiModelProperty(value="手机号")
    private String telphone;
    /**
     * 姓名
     */
	@ApiModelProperty(value="姓名")
    private String realName;
    /**
     * 是否vip 1-是 2-否
     */
	@ApiModelProperty(value="是否vip 1-是 2-否")
    private Byte isVIP;
    /**
     * vip到期时间
     */
	@ApiModelProperty(value="vip到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    /**
     * 最后登录时间
     */
	@ApiModelProperty(value="登陆时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;
    /**
     * 客户经理ID
     */
	@ApiModelProperty(value="客户经理ID")
    private Integer managerId;
}
