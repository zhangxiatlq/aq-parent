package com.aq.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lijie
 * @ClassName: RedisDistributionLock
 * @Description:分布式锁redis实现
 * @date 2018年1月30日 下午9:48:25
 */
@Component
@Slf4j
public class RedisDistributionLock {

    // 锁的有效时间(单位毫秒)
    private final long TIMOUT = 30 * 1000;
    // 减少活锁单位毫秒)
    private final long SLEEP_TIME = 10;

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    @Autowired
    private @SuppressWarnings("rawtypes")
    RedisTemplate redisTemplate;

    private final ConcurrentMap<String, String> REQUEST_SIGNMAP = new ConcurrentHashMap<String, String>(128);
    
    /**
     * @param @param  lockKey
     * @param @param  requestId
     * @param @return
     * @param @throws InterruptedException    设定文件
     * @return boolean    返回类型
     * @throws InterruptedException
     * @throws
     * @Title: tryLock
     * @Description: 获取锁
     * @author lijie
     */
	public void lock(String lockKey) {
		Jedis jedis = getJedis();
		lockKey = getLockKey(lockKey);
		String requestId = UUID.randomUUID().toString();
		try {
			while (true) {
				// 设置互斥量
				String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, TIMOUT);
				if (LOCK_SUCCESS.equals(result)) {
					REQUEST_SIGNMAP.put(getThreadKey(lockKey), requestId);
					break;
				}
				sleep(SLEEP_TIME);
			}
		} finally {
			jedis.close();
		}
	}


    /**
     * 获取锁
     *
     * @param lockKey          需要锁定的key
     * @param time             锁的超时时间
     * @param sleepMillisecond 业务系统自定义未获取到锁时，休眠多少毫秒再取竞争锁，默认为10ms
     * @return boolean  true  获取到锁  false 未获取到锁
     * @author 郑朋
     * @create 2018/2/26
     */
    public boolean tryLock(String lockKey, final long time, long sleepMillisecond) {
        if (StringUtils.isBlank(lockKey) || time <= 0) {
            return false;
        }
        Jedis jedis = getJedis();
        lockKey = getLockKey(lockKey);
        String requestId = UUID.randomUUID().toString();
        try {
            while (true) {
                // 设置互斥量
                String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, time);
                if (LOCK_SUCCESS.equals(result)) {
                	REQUEST_SIGNMAP.put(getThreadKey(lockKey), requestId);
                    return true;
                } else if (System.currentTimeMillis() - time > 0) {
                    return false;
                }
                sleep(sleepMillisecond);
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * @param @return 设定文件
     * @return Jedis    返回类型
     * @throws
     * @Title: initJedis
     * @Description: 通过反射得到Jedis实例
     * @author lijie
     */
    private Jedis getJedis() {
        Field jedisField = ReflectionUtils.findField(JedisConnection.class, "jedis");
        ReflectionUtils.makeAccessible(jedisField);
        return (Jedis) ReflectionUtils.getField(jedisField,
                redisTemplate.getConnectionFactory().getConnection());
    }

    /**
     * @param @param  lockKey
     * @param @param  requestId
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: unLock
     * @Description: 释放锁
     * @author lijie
     */
	public boolean unLock(String lockKey) {
		lockKey = getLockKey(lockKey);
		Jedis jedis = getJedis();
		String requestId = REQUEST_SIGNMAP.get(getThreadKey(lockKey));
		// 使用lua脚本实现原子删除
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		try {
			Object result = jedis.eval(script, Collections.singletonList(lockKey),
					Collections.singletonList(requestId));
			return RELEASE_SUCCESS.equals(result);
		} finally {
			jedis.close();
			REQUEST_SIGNMAP.remove(getThreadKey(lockKey), requestId);
		}
	}

	private String getThreadKey(final String lockKey) {

		return lockKey + Thread.currentThread().getId();
	}
	
    private void sleep(long ms) {
        try {
            if (ms <= 0) {
                ms = SLEEP_TIME;
            }
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.warn("the thread:{} sleeping was interrupted. {}", Thread.currentThread().getId(), e);
        }
    }

    /**
     * @param @param  key
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getLockKey
     * @Description: 分布式锁key
     * @author lijie
     */
    private String getLockKey(String key) {

        return "DISTRIBUTION_LOCK_" + key;
    }
}
