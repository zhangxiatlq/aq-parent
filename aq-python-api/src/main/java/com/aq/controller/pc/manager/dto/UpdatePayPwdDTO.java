package com.aq.controller.pc.manager.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: UpdatePayPwdDTO
 * @Description: 修改支付密码传输实体
 * @author: lijie
 * @date: 2018年2月10日 下午1:30:00
 */
@Data
@ApiModel(value="修改支付密码传输实体")
public class UpdatePayPwdDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8031120898421719753L;
	
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号", required = true)
	private String telphone;
	/**
	 * 支付密码
	 */
	@ApiModelProperty(value = "支付密码", required = true)
	private String payPwd;
	/**
	 * 短信验证码
	 */
	@ApiModelProperty(value = "短信验证码", required = true)
	private String smsCode;
}
