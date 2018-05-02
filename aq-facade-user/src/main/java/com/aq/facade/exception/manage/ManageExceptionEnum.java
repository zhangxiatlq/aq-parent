package com.aq.facade.exception.manage;

import com.aq.util.result.RespCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: 客户经理异常枚举
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月10日 下午12:44:30
 */
@Getter
@AllArgsConstructor
public enum ManageExceptionEnum implements RespCode {

	ADD_MANAGE_ERROR("30001","添加客户经理异常"),
	UPDATE_MANAGELOGINPWD_ERROR("30002","修改客户经理登录密码异常"),
	UPDATE_MANAGEPAYPWD_ERROR("30003","修改/设置客户经理支付密码异常"),
	UPDATE_CUSTOMER_OPENID_ERROR("30003","绑定客户经理微信openId异常"),
	ACCOUNT_ALREADY_BIND("30004","账号已绑定其它微信"),
	OPENID_EXISTS("30005","微信已绑定其它账号");
	
	private String code;

	private String message;

	@Override
	public String getMsg() {
		return message;
	}

	@Override
	public String getCode() {
		return code;
	}
}
