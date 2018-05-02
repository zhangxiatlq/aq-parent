package com.aq.facade.dto.customer;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.aq.core.base.BaseValidate;

import lombok.Data;
/**
 * 
 * @ClassName: UpdateCustomerDTO
 * @Description: 修改客户信息传输实体
 * @author: lijie
 * @date: 2018年2月11日 下午9:57:40
 */
@Data
public class UpdateCustomerDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -2157646329774125999L;
	/**
	 * 客户ID
	 */
	@NotNull(message = "客户ID不能为空")
	private Integer id;
	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * 是否vip 1-是 2-否
	 */
	private Byte isVIP;
	/**
	 * vip到期时间
	 */
	private Date endTime;
	/**
	 * Openid 绑定微信
	 */
	private String openId;
}
