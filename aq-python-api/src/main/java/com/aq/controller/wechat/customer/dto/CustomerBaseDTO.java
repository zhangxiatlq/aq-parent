package com.aq.controller.wechat.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * 
 * @ClassName: CustomerBaseDTO
 * @Description: 客户相关信息数据
 * @author: lijie
 * @date: 2018年2月8日 下午4:54:54
 */
@Data
public class CustomerBaseDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2541386446835383257L;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码（使用rsa公钥加密）,{password:密码}", required = true)
	private String password;
	/**
	 * 时间字符串
	 */
	@ApiModelProperty(value = "时间戳", hidden = true)
	private String timestamp;
	/**
	 * 签名
	 */
	@ApiModelProperty(value = "签名（签名采用rsa公钥加密，数据顺序已后面备注的为准），备注签名数据（json）：{account:手机号（如果是登录则就是手机号或则用户名）,password:密码（加密后的密码）,timestamp:时间戳 ,openId:openId}", hidden = true)
	private String sign;
}
