package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
/**
 * 
 * @ClassName: WeChatBindDTO
 * @Description: 微信绑定传输数据
 * @author: lijie
 * @date: 2018年2月23日 下午12:50:43
 */
@Data
@ApiModel(value="微信绑定传输数据")
public class WeChatBindDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 5898245360019895313L;
	/**
	 * 账号：手机号或则用户名
	 */
	@ApiModelProperty(value = "账号：手机号或则用户名", required = true)
	@NotBlank(message = "手机号或则用户名不能为空")
	private String account;
	/**
	 * 微信openId
	 */
	@ApiModelProperty(value = "微信openId", required = true)
	@NotBlank(message = "微信openId不能为空")
	private String openId;
	/**
	 * 角色：2、客户 3、客户经理
	 */
	@ApiModelProperty(value = "角色：2、客户 3、客户经理", required = true)
	@NotNull(message = "角色不能为空")
	private Byte roleCode;
}
