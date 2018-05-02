package com.aq.facade.dto.manage;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: LoginDTO
 * @Description: 登录传输DTO
 * @author: lijie
 * @date: 2018年2月9日 下午2:16:48
 */
@Data
@ApiModel(value="登录传输数据")
public class LoginDTO extends BaseValidate {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 账户
	 */
	@NotBlank(message="手机号或则用户名不能为空")
	@ApiModelProperty(value="登录账号",required=true)
	private String account;
	/**
	 * 密码
	 */
	@NotBlank(message="登录密码不能为空")
	@ApiModelProperty(value="密码",required=true)
	private String password;
}
