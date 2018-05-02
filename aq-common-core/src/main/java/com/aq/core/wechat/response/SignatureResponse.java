package com.aq.core.wechat.response;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SignatureResponse
 * @Description: 
 * @author: lijie
 * @date: 2018年1月30日 上午10:55:57
 */
@Data
public class SignatureResponse implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 状态码
	 */
	private int errcode;
	/**
	 * 状态吗描述
	 */
	private String errmsg;
	
	private String ticket;
	
	private int expires_in;
}
