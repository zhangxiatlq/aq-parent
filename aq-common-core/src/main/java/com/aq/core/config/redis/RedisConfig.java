package com.aq.core.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;
import com.aq.core.config.properties.DefRedisProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @项目：mammon-parent
 * @描述：redis配置类
 * @创建时间：2017/11/26
 * @Copyright @2017 by Mr.chang
 * @author Mr.chang
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig {

	@Autowired
	private DefRedisProperties defRedisProperties;
	
    @Bean
    public KeyGenerator simpleKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager cahceManager = new RedisCacheManager(redisTemplate);
		// 设置默认失效时间（单位秒）
		cahceManager.setDefaultExpiration(7200L);
		return cahceManager;
	}

	@Bean
	public RedisConnectionFactory defRedisConnectionFactory() {
		log.info("redis 公用连接配置信息 RedisProperties = {}", JSON.toJSONString(defRedisProperties));
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(defRedisProperties.getPool().getMaxActive());
		poolConfig.setMaxIdle(defRedisProperties.getPool().getMaxIdle());
		poolConfig.setMinIdle(defRedisProperties.getPool().getMinIdle());
		poolConfig.setMaxWaitMillis(defRedisProperties.getPool().getMaxWait());
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setDatabase(defRedisProperties.getDatabase());
		if (StringUtils.isNotBlank(defRedisProperties.getHost())) {
			jedisConnectionFactory.setHostName(defRedisProperties.getHost());
		}
		if (StringUtils.isNotBlank(defRedisProperties.getPassword())) {
			jedisConnectionFactory.setPassword(defRedisProperties.getPassword());
		}
		jedisConnectionFactory.setPort(defRedisProperties.getPort());
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory defRedisConnectionFactory) {
		return new StringRedisTemplate(defRedisConnectionFactory);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory defRedisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		redisTemplate.setConnectionFactory(defRedisConnectionFactory);
		redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public RedisMessageListenerContainer topicContainer(RedisConnectionFactory defRedisConnectionFactory) {
		RedisMessageListenerContainer topicContainer = new RedisMessageListenerContainer();
		topicContainer.setConnectionFactory(defRedisConnectionFactory);
		// 设置订阅等待时间（5分钟未接收到发布信息就会关闭订阅）
		topicContainer.setMaxSubscriptionRegistrationWaitingTime(300000L);
		return topicContainer;
	}
}
