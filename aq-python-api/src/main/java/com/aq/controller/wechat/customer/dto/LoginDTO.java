package com.aq.controller.wechat.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: CustomerLoginDTO
 * @Description: 登录传输数据
 * @author: lijie
 * @date: 2018年2月8日 下午6:51:30
 */
@Data
@ApiModel(value="客户登录数据信息")
public class LoginDTO extends CustomerBaseDTO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名或则手机号
	 */
	@ApiModelProperty(value = "用户名或则手机号", required = true)
	private String account;
}
