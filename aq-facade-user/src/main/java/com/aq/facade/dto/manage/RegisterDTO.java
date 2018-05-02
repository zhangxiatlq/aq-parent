package com.aq.facade.dto.manage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: RegisterDTO
 * @Description: 注册客户经理
 * @author: lijie
 * @date: 2018年2月9日 下午2:33:33
 */
@Data
@ApiModel(value="注册客户经理传输数据")
public class RegisterDTO extends RegisterBaseDTO {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -7554554249834794477L;
	/**
	 * 短信验证码
	 */
	@ApiModelProperty(value="短信验证码",required=true)
	private String smsCode;





}
