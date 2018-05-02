package com.aq.core.wechat.response;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
/**
 * 
 * @ClassName: WebAuthorizeResponse
 * @Description: 微信授权回调access_token响应数据
 * @author: lijie
 * @date: 2018年2月23日 上午10:48:18
 */
@Data
public class WebAuthorizeResponse implements ResponseState {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 9078526757719617533L;
	/**
	 * 状态码 0：成功
	 */
	private String errcode;
	/**
	 * 状态描述
	 */
	private String errmsg;
	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	private String access_token;
	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private String expires_in;
	/**
	 * 用户刷新access_token
	 */
	private String refresh_token;
	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	private String openid;
	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;
	
	@Override
	public boolean isSuccess() {

		return StringUtils.isNotBlank(this.openid);
	}
}
