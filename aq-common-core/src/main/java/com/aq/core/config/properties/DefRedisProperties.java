package com.aq.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * 
* @ClassName: RedisProperties 
* @Description: redis 配置 
* @author lijie
* @date 2017年8月22日 下午3:22:23 
*
 */
@Component("defRedisProperties")
@ConfigurationProperties(prefix = "spring.redis")
public class DefRedisProperties {
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
	private int database = 0;
	/**
	 * 连接池配置
	 */
	private Pool pool = new Pool();
	
	public static class Pool {
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
		
		public Pool() {}

		public int getMaxIdle() {
			return this.maxIdle;
		}

		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		public int getMinIdle() {
			return this.minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public int getMaxActive() {
			return this.maxActive;
		}

		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}

		public int getMaxWait() {
			return this.maxWait;
		}

		public void setMaxWait(int maxWait) {
			this.maxWait = maxWait;
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Pool getPool() {
		return pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}
	
}
