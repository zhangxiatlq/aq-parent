package com.aq.facade.dto.customer;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;

import lombok.Data;
/**
 * 
 * @ClassName: AddCustomerDTO
 * @Description: 添加客户入参
 * @author: lijie
 * @date: 2018年2月8日 上午11:28:09
 */
@Data
public class AddCustomerDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空")
	private String username;
	/**
	 * 手机号
	 */
	@NotBlank(message="手机号不能为空")
	private String telphone;
	/**
	 * 登录密码
	 */
	@NotBlank(message="登录密码不能为空")
	private String password;
	/**
	 * 创建人id
	 */
	@NotNull(message="创建人id不能为空")
	private Integer createrId;
	/**
	 * 姓名
	 */
	@NotBlank(message="姓名不能为空")
	private String realName;
	
	@NotBlank(message="微信标识不能为空")
	private String openId;

	private Integer idCode;
}
