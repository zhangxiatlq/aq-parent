package com.aq.facade.dto.manage;

import com.aq.core.base.BaseValidate;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 
 * @ClassName: PayPwdDTO
 * @Description: 支付密码
 * @author: lijie
 * @date: 2018年2月10日 下午1:53:48
 */
@Data
public class PayPwdDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2461962953963525563L;
	/**
	 * 操作人ID
	 */
	@NotNull(message="操作人ID不能为空")
	private Integer operId;
	/**
	 * 手机号
	 */
	@NotBlank(message="手机号不能为空")
	private String telphone;
	/**
	 * 支付密码
	 */
	@NotBlank(message="支付密码不能为空")
	private String payPwd;
}
