package com.aq.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 
 * @ClassName: WeChatCoreProperties
 * @Description: 微信核心配置
 * @author: lijie
 * @date: 2018年3月15日 下午9:12:23
 */
@Data
@PropertySource("classpath:wechat-core.properties")
@ConfigurationProperties(prefix = "wechat")
@Component
public class WeChatCoreProperties {
	/**
	 * 服务号应用密码
	 */
	private String secret;
	/**
	 * 服务号应用ID
	 */
	private String appid;
	/**
	 * 商户号
	 */
	private String mchid;
	/**
	 * API密钥(商户密钥)
	 */
	private String apikey;
	/**
	 * 视图跳转地址
	 */
	private String viewUrl;
	/**
	 * 微信 网页授权地址
	 */
	private String jumpAuthorize;
	/**
	 * 微信支付异步回调地址
	 */
	private String async;
	/**
	 * 微信网页授权回调地址
	 */
	private String authorize;
	/**
	 * 微信公众平台Token
	 */
	private String token;
}
