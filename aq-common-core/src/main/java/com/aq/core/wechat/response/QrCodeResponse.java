package com.aq.core.wechat.response;

import lombok.Data;
/**
 * 
 * @ClassName: QrCodeResponse
 * @Description: 二维码生成返回数据
 * @author: lijie
 * @date: 2018年3月13日 下午5:51:08
 */
@Data
public class QrCodeResponse {
	/**
	 * 二维码生成请求标识
	 */
	private String ticket;
	/**
	 * 有效时间
	 */
	private String expire_seconds;
	/**
	 * 自行生成二维码需解析url
	 */
	private String url;
}
