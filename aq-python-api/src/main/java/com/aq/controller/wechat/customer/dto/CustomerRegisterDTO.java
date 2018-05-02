package com.aq.controller.wechat.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: CustomerRegisterDTO
 * @Description: 注册传输数据
 * @author: lijie
 * @date: 2018年2月8日 下午4:48:12
 */
@Data
@ApiModel(value="注册传输数据")
public class CustomerRegisterDTO extends CustomerBaseDTO {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 3894284058671730483L;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名", required = true)
	private String username;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号", required = true)
	private String telphone;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名", required = true)
	private String realName;
	/**
	 * 短信验证码
	 */
	@ApiModelProperty(value = "短信验证码", required = true)
	private String smsCode;
	/**
	 * 客户经理id编码
	 */
	@ApiModelProperty(value="客户经理id编码",required=true)
	private Integer idCode;
}
