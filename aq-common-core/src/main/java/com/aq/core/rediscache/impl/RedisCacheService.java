/**  
 * @Title:  RedisService.java   
 * @Package com.ph.shopping.common.core.cache.redis   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 李杰    
 * @date:   2017年5月14日 下午6:10:54   
 * @version V1.0 
 * @Copyright: 2017
 */
package com.aq.core.rediscache.impl;

import com.aq.core.rediscache.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisService
 * @Description:redis 相关基础操作
 * @author: 李杰
 * @date: 2017年5月14日 下午6:10:54
 * @Copyright: 2017
 */
@Component
public class RedisCacheService<K, V> implements ICacheService<K, V> {

	@Autowired
	private RedisTemplate<K, V> redisTemplate;

	@Override
	public void set(final K key, V value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(final K key, V value, Long expireTime, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, expireTime, unit);
	}

	@Override
	public void removes(final K[] keys) {
		for (K key : keys) {
			remove(key);
		}
	}

	@Override
	public void remove(final K key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	@Override
	public boolean exists(final K key) {
		return redisTemplate.hasKey(key);
	}

	@Override
	public void removePattern(final K pattern) {
		Set<K> keys = redisTemplate.keys(pattern);
		if (keys != null && keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}

	@Override
	public Object get(final K key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void hmSet(K key, Object hashKey, V value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}
	@Override
	public Set<Object> keys(K key) {
		
		return redisTemplate.opsForHash().keys(key);
	}
	@Override
	public Object hmGet(K key, Object hashKey) {
		
		return redisTemplate.opsForHash().get(key, hashKey);
	}

	@Override
	public Long hmRemove(K key, Object... hashKeys) {
		
		return redisTemplate.opsForHash().delete(key, hashKeys);
	}

	@Override
	public boolean hmExists(K key, Object hashKey) {

		return redisTemplate.opsForHash().hasKey(key, hashKey);
	}

	@Override
	public void lPush(K k, V value) {
		redisTemplate.opsForList().leftPush(k, value);
	}

	@Override
	public List<V> lRange(K k, long l, long lO) {

		return redisTemplate.opsForList().range(k, l, lO);
	}

	@Override
	public List<V> lRange(K k) {
		
		return redisTemplate.opsForList().range(k, 0, -1);
	}

	@Override
	public V rPop(K k){
		return redisTemplate.opsForList().rightPop(k);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Long sAdd(K key, V value) {
		return redisTemplate.opsForSet().add(key, value);
	}

	@Override
	public Set<V> members(K key) {
		return redisTemplate.opsForSet().members(key);
	}

	@Override
	public Long sRemove(K key, Object... values) {
		return redisTemplate.opsForSet().remove(key, values);
	}

	@Override
	public boolean isMember(K key, Object o) {

		return redisTemplate.opsForSet().isMember(key, o);
	}

	@Override
	public Boolean zadd(K key, V value, double score) {
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	@Override
	public Set<V> zrange(K key) {

		return redisTemplate.opsForZSet().range(key, 0, -1);
	}

	@Override
	public Set<V> zrangeByScore(K key, double min, double max) {

		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	@Override
	public boolean expire(final K key, final long timeout, final TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	@Override
	public Long getExpire(final K key) {
		return redisTemplate.getExpire(key);
	}

	@Override
	public Long getExpire(final K key, final TimeUnit timeUnit) {
		return redisTemplate.getExpire(key, timeUnit);
	}

	@Override
	public void convertAndSend(K key, V value) {
		redisTemplate.convertAndSend(key.toString(), value);
	}

	@Override
	public List<Object> values(K key) {
		
		return redisTemplate.opsForHash().values(key);
	}

	@Override
	public Long listRemove(K key, int count, V value) {
		return redisTemplate.opsForList().remove(key, count, value);
	}

	@Override
	public Long listSize(K k) {
		return redisTemplate.opsForList().size(k);
	}

}
