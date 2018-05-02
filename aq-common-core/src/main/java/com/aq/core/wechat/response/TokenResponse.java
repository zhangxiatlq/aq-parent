package com.aq.core.wechat.response;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: TokenResponse
 * @Description: 微信 acc token响应数据
 * @author: lijie
 * @date: 2018年1月30日 上午10:53:07
 */
@Data
public class TokenResponse implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * token
	 */
	private String access_token;
	/**
	 * 有效时间
	 */
	private Integer expires_in;
}
