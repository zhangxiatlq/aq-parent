package com.aq.facade.dto.customer;

import com.aq.core.base.BaseValidate;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
/**
 * 
 * @ClassName: CustomerLoginDTO
 * @Description: 登录传输dto
 * @author: lijie
 * @date: 2018年2月8日 下午12:35:15
 */
@Data
public class CustomerLoginDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8417549430823403914L;
	/**
	 * 账户（手机号或则用户名）
	 */
	@NotBlank(message="手机号或则用户名不能为空")
	private String account;
	/**
	 * 登录密码
	 */
	@NotBlank(message="登录密码不能为空")
	private String password;
}
