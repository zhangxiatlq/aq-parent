package com.aq.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @ClassName: RedisConfig
 * @Description: redis配置
 * @author: lijie
 * @date: 2018年2月27日 下午7:40:23
 */
@Slf4j
@Configuration
@EnableCaching
public class PyRedisConfig {

	@Autowired
	private RedisProperties redis;
	
	@Bean
	public RedisConnectionFactory pyjedisConnectionFactory() {
		log.info("redis 连接配置信息 RedisProperties = {}", JSON.toJSONString(redis));
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(redis.getMaxActive());
		poolConfig.setMaxIdle(redis.getMaxIdle());
		poolConfig.setMinIdle(redis.getMinIdle());
		poolConfig.setMaxWaitMillis(redis.getMaxWait());
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setDatabase(redis.getDatabase());
		if (StringUtils.isNotBlank(redis.getHost())) {
			jedisConnectionFactory.setHostName(redis.getHost());
		}
		if (StringUtils.isNotBlank(redis.getPassword())) {
			jedisConnectionFactory.setPassword(redis.getPassword());
		}
		jedisConnectionFactory.setPort(redis.getPort());
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}

	@Bean
	public RedisMessageListenerContainer topicContainer(RedisConnectionFactory pyjedisConnectionFactory) {
		RedisMessageListenerContainer topicContainer = new RedisMessageListenerContainer();
		topicContainer.setConnectionFactory(pyjedisConnectionFactory);
		return topicContainer;
	}

	@Bean
	public StringRedisTemplate pyRedisTemplate(RedisConnectionFactory pyjedisConnectionFactory) {
		return new StringRedisTemplate(pyjedisConnectionFactory);
	}
}