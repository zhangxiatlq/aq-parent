package com.aq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 
* @ClassName: RedisProperties 
* @Description: redis 配置 
* @author lijie
* @date 2017年8月22日 下午3:22:23 
*
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
@PropertySource("classpath:pyredis.properties")
public class RedisProperties {
	/**
	 * 主机地址
	 */
	private String host = "localhost";
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 端口号
	 */
	private int port = 6379;
	/**
	 * 数据库
	 */
	private int database = 7;
	/**
	 * 最大能够保持idel状态的对象数
	 */
	private int maxIdle = 500;
	/**
	 * 最小能够保持idel状态的对象数
	 */
	private int minIdle = 0;
	/**
	 * 最大活动对象数
	 */
	private int maxActive = 200;
	/**
	 * 最大等待时间
	 */
	private int maxWait = -1;
	
}
