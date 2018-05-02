package com.aq.core.wechat.result;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SignatureResult
 * @Description: 微信签名返回实体
 * @author: lijie
 * @date: 2018年1月20日 下午2:19:32
 */
@Data
public class SignatureResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6362266892987667251L;
	/**
	 * 生成签名的时间戳
	 */
	private String timestamp;
	/**
	 * 生成签名的随机串
	 */
	private String nonceStr;
	/**
	 * 签名
	 */
	private String signature;
	
	@Override
	public String toString() {
		return "SignatureResult [timestamp=" + timestamp + ", nonceStr=" + nonceStr + ", signature=" + signature + "]";
	}
	
	
}
