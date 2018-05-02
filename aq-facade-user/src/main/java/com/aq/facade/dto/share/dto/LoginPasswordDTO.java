package com.aq.facade.dto.share.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.aq.core.base.BaseValidate;
import com.aq.facade.enums.PwdOperTypeEnum;

import lombok.Data;
/**
 * 
 * @ClassName: LoginPasswordDTO
 * @Description: 登录密码传输实体
 * @author: lijie
 * @date: 2018年2月10日 上午11:59:54
 */
@Data
public class LoginPasswordDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -6739773604492429244L;
	/**
	 * 修改人ID
	 */
	private Integer updaterId;
	/**
	 * 操作
	 */
	@NotNull(message = "操作枚举不能为空")
	private PwdOperTypeEnum type;
	/**
	 * 手机号
	 */
	@NotBlank(message="手机号不能为空")
	private String telphone;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 新密码
	 */
	@NotBlank(message="新密码不能为空")
	private String newPassword;
}
