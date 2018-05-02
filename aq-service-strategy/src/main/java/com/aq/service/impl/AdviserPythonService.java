package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.constant.CacheKey;
import com.aq.facade.contant.AdviserEnum;
import com.aq.facade.entity.AdviserPosition;
import com.aq.facade.entity.AdviserPush;
import com.aq.facade.entity.AdviserTradeRecord;
import com.aq.facade.vo.AdviserPositionBaseVO;
import com.aq.facade.vo.AdviserPositionVO;
import com.aq.facade.vo.AdviserPushVO;
import com.aq.facade.vo.AdviserTradeRecordVO;
import com.aq.mapper.AdviserPushMapper;
import com.aq.mapper.AdviserTradeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * java python 相关代码
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
@Service
@Slf4j
public class AdviserPythonService {

    @Autowired
    private AdviserPushMapper adviserPushMapper;

    @Autowired
    private StringRedisTemplate pyRedisTemplate;


    @Autowired
    private AdviserTradeRecordMapper adviserTradeRecordMapper;

    /**
     * 修改可以资产
     *
     * @param adviserTradeRecord
     * @param pushId
     * @param jsonObject
     * @return void
     * @author 郑朋
     * @create 2018/3/12
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateAdviserBuy(AdviserTradeRecord adviserTradeRecord, Integer pushId, JSONObject jsonObject) {
        log.info("修改可以资产：adviserTradeRecord={},pushId={},jsonObject={}", JSON.toJSONString(adviserTradeRecord), pushId, jsonObject.toJSONString());
        //更新数据库总资产和可用资产
        adviserTradeRecordMapper.updateByPrimaryKeySelective(adviserTradeRecord);
        //更新数据库推送记录状态
        AdviserPush adviserPush = new AdviserPush();
        adviserPush.setId(pushId);
        adviserPush.setTradeStatus(AdviserEnum.TRADE_CANCEL.getCode());
        adviserPushMapper.updateByPrimaryKeySelective(adviserPush);
        Jedis jedis = getJedis();
        try {
            Transaction transaction = jedis.multi();
            //更新缓存中的可用资产数据
            transaction.hset(CacheKey.ADVISER_TRADE_RECORD.getKey()
                    , getAdviserKey(adviserTradeRecord.getAdviserId())
                    , jsonObject.toJSONString());
            //删除缓存中的推送记录
            transaction.hdel(CacheKey.INVESTMENT_CONSIGNMENT.getKey(), getPushKey(adviserTradeRecord.getAdviserId(), pushId));
            transaction.exec();
        } finally {
            jedis.close();
        }
    }

    /**
     * 撤单 -- 卖出股票数据处理
     *
     * @param adviserTradeRecord
     * @param adviserPushVO
     * @return void
     * @author 郑朋
     * @create 2018/3/16
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateAdviserSell(AdviserTradeRecord adviserTradeRecord, AdviserPushVO adviserPushVO) {
        //改变状态
        AdviserPush adviserPush = new AdviserPush();
        adviserPush.setId(adviserPushVO.getPushId());
        adviserPush.setTradeStatus(AdviserEnum.TRADE_CANCEL.getCode());
        adviserPushMapper.updateByPrimaryKeySelective(adviserPush);
        Object object = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_POSITION_UPDATE.getKey(),
                getAdviserKey(adviserTradeRecord.getAdviserId(), adviserPushVO.getSharesCode()));
        log.info("撤单-卖出 读取redis 数据 position={}", JSON.toJSONString(object));
        //持仓修改
        Jedis jedis = getJedis();
        try {
            Transaction transaction = jedis.multi();
            //更新缓存中的可用资产数据
            JSONObject jsonObject = JSON.parseObject(object.toString());
            int holdingNo = jsonObject.getInteger("holdingNo") + adviserPushVO.getTradeNumber();
            int sellFreezeNum = jsonObject.getInteger("sellFreezeNum") - adviserPushVO.getTradeNumber();
            jsonObject.put("holdingNo", holdingNo);
            jsonObject.put("sellFreezeNum", sellFreezeNum);
            log.info("撤单-卖出 写入redis 数据 position={}", jsonObject.toJSONString());
            transaction.hset(CacheKey.ADVISER_POSITION_UPDATE.getKey()
                    , getAdviserKey(adviserTradeRecord.getAdviserId(), adviserPushVO.getSharesCode())
                    , jsonObject.toJSONString());
            //删除缓存中的推送记录
            transaction.hdel(CacheKey.INVESTMENT_CONSIGNMENT.getKey()
                    , getPushKey(adviserTradeRecord.getAdviserId(), adviserPushVO.getPushId()));
            transaction.exec();
        } finally {
            jedis.close();
        }
    }


    /**
     * 修改所有的持仓冻结股票数为0
     *
     * @param
     * @return void
     * @author 郑朋
     * @create 2018/3/13
     */
    public void updatePosition() {
        Set<Object> setKeys = pyRedisTemplate.opsForHash().keys(CacheKey.ADVISER_POSITION_UPDATE.getKey());
        for (Object key : setKeys) {
            try {
                Object value = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_POSITION_UPDATE.getKey(), key);
                log.info("股票持仓redis 数据 key={}，value={}", JSON.toJSONString(key), JSON.toJSONString(value));
                if (null != value) {
                    JSONObject jsonObject = JSON.parseObject(value.toString());
                    //更新股票冻结数为 0
                    jsonObject.put("freezeNum", 0);
                    log.info("修改股票持仓redis 数据 key={}，value={}", JSON.toJSONString(jsonObject));
                    pyRedisTemplate.opsForHash().put(CacheKey.ADVISER_POSITION_UPDATE.getKey(), key, jsonObject.toJSONString());
                }
                Object value1 = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(), key);
                log.info("股票持仓redis 数据 key={}，value={}", JSON.toJSONString(key), JSON.toJSONString(value));
                if (null != value1) {
                    JSONObject jsonObject = JSON.parseObject(value1.toString());
                    //更新股票冻结数为 0
                    jsonObject.put("freezeNum", 0);
                    log.info("修改股票持仓redis 数据 key={}，value={}", JSON.toJSONString(jsonObject));
                    pyRedisTemplate.opsForHash().put(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(), key, jsonObject.toJSONString());
                }
            } catch (Exception e) {
                log.error("股票持仓redis 异常 e={} 数据 key={}", e, JSON.toJSONString(key));
            }
        }
    }


    /**
     * 删除投顾会缓存数据
     *
     * @param adviserId
     * @param positionList
     * @param adviserPushList
     * @return void
     * @author 郑朋
     * @create 2018/3/13
     */
    public void deleteAdviser(Integer adviserId, List<AdviserPosition> positionList, List<AdviserPush> adviserPushList) {
        Jedis jedis = getJedis();
        try {
            Transaction transaction = jedis.multi();
            //删除委托记录
            if (CollectionUtils.isNotEmpty(adviserPushList)) {
                String[] pushKeys = new String[adviserPushList.size()];
                for (int i = 0; i < adviserPushList.size(); i++) {
                    pushKeys[i] = getPushKey(adviserId, adviserPushList.get(i).getId());
                }
                transaction.hdel(CacheKey.INVESTMENT_CONSIGNMENT.getKey(), pushKeys);
            }
            //删除持仓数据
            if (CollectionUtils.isNotEmpty(positionList)) {
                String[] positionKeys = new String[positionList.size()];
                for (int i = 0; i < positionList.size(); i++) {
                    positionKeys[i] = getAdviserKey(adviserId, positionList.get(i).getSharesCode());
                }
                transaction.hdel(CacheKey.ADVISER_POSITION_UPDATE.getKey(), positionKeys);
                transaction.hdel(CacheKey.ADVISER_POSITION.getKey(), positionKeys);
                transaction.hdel(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(), positionKeys);
            }
            //删除资产记录
            transaction.hdel(CacheKey.ADVISER_TRADE_RECORD_REALTIME.getKey(), getAdviserKey(adviserId));
            transaction.hdel(CacheKey.ADVISER_TRADE_RECORD.getKey(), getAdviserKey(adviserId));
            transaction.hdel(CacheKey.DAILY_ADVISER_TRADE_RECORD.getKey(), getAdviserKey(adviserId));
            transaction.exec();
        } catch (Exception e) {
            log.error("删除投顾会缓存数据异常，e={}", e);
            throw e;
        } finally {
            jedis.close();
        }

    }


    /**
     * 获取推送记录 key
     *
     * @param adviserId
     * @param pushId
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/15
     */
    public static String getPushKey(Integer adviserId, Integer pushId) {
        if (null != adviserId && null != pushId) {
            return "adviserId_" + adviserId + "_" + pushId;
        }
        return null;
    }

    /**
     * 获取推送记录 key
     *
     * @param pushId
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/20
     */
    public static String getPushKey(Integer pushId) {
        if (null != pushId) {
            return pushId.toString();
        }
        return null;
    }

    private Jedis getJedis() {
        Field jedisField = ReflectionUtils.findField(JedisConnection.class, "jedis");
        ReflectionUtils.makeAccessible(jedisField);
        return (Jedis) ReflectionUtils.getField(jedisField, pyRedisTemplate.getConnectionFactory().getConnection());
    }


    /**
     * 获取缓存key -- 收益记录
     *
     * @param adviserId
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/13
     */
    public static String getAdviserKey(Integer adviserId) {
        return adviserKey(adviserId, null);
    }


    /**
     * 获取缓存key -- 持仓
     *
     * @param adviserId
     * @param code
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/13
     */
    public static String getAdviserKey(Integer adviserId, String code) {
        return adviserKey(adviserId, code);
    }

    private static String adviserKey(Integer adviserId, String code) {
        if (null != adviserId) {
            String key = "adviserId_" + adviserId;
            if (StringUtils.isNotBlank(code)) {
                key += "-" + code;
            }
            return key;
        }
        return null;
    }


    /**
     * @param @param  adviserId
     * @param @param  code
     * @param @return 参数
     * @return AdviserPositionVO 返回类型
     * @throws
     * @Title: getAdviserPositions
     * @Description: 获取投顾持仓数据
     * @author lijie
     */
    public AdviserPositionVO getAdviserPositions(final Integer adviserId, final String code) {
        AdviserPositionVO result = null;
        Object obj = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(),
                getAdviserKey(adviserId, code));
        if (null != obj) {
            result = JSONObject.parseObject(obj.toString(), AdviserPositionVO.class);
        }
        return result;
    }
    /**
     * 
    * @Title: getAdviserPositions  
    * @Description: TODO 
    * @param: @param keys
    * @param: @return
    * @return List<AdviserPositionVO>
    * @author lijie
    * @throws
     */
	public List<AdviserPositionBaseVO> getAdviserPositions(final List<String> keys) {
		List<AdviserPositionBaseVO> result = null;
		if (CollectionUtils.isNotEmpty(keys)) {
			Collection<Object> hashKeys = new ArrayList<>(keys);
			List<Object> objs = pyRedisTemplate.opsForHash().multiGet(CacheKey.ADVISER_POSITION_UPDATE_REALTIME.getKey(),
					hashKeys);
			if (CollectionUtils.isNotEmpty(objs)) {
				result = new ArrayList<>();
				for (int i = 0; i < objs.size(); i++) {
					result.add(JSONObject.parseObject(objs.get(i).toString(), AdviserPositionBaseVO.class));
				}
			}
		}
		return result;
	}
    /**
     * @param @param  adviserId
     * @param @return 参数
     * @return AdviserTradeRecord 返回类型
     * @throws
     * @Title: getAdviserTradeRecord
     * @Description: 得到投顾指标信息
     * @author lijie
     */
	public AdviserTradeRecordVO getAdviserTradeRecord(final Integer adviserId) {
		AdviserTradeRecordVO result = null;
		Object obj = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_TRADE_RECORD.getKey(), getAdviserKey(adviserId));
		if (null != obj) {
			result = JSONObject.parseObject(obj.toString(), AdviserTradeRecordVO.class);
            Object obj1 = pyRedisTemplate.opsForHash().get(CacheKey.ADVISER_TRADE_RECORD_REALTIME.getKey(), getAdviserKey(adviserId));
            if (null != obj1) {
                JSONObject jsonObject = JSON.parseObject(obj1.toString());
                result.setTotalAssets(jsonObject.getString("totalAssets"));
                result.setMarketValue(jsonObject.getString("marketValue"));
                result.setTodayRate(jsonObject.getString("todayRate"));

            }
		}
		return result;
	}

    /**
     * 获取推送消息
     *
     * @param pushId
     * @return com.aq.facade.entity.AdviserPush
     * @author 郑朋
     * @create 2018/3/20
     */
    public AdviserPush getAdviserPush(Integer pushId) {
        AdviserPush adviserPush = null;
        Object obj = pyRedisTemplate.opsForHash().get(CacheKey.INVESTMENT_CONSIGNMENT_PUSH.getKey(), getPushKey(pushId));
        if (null != obj) {
            adviserPush = JSONObject.parseObject(obj.toString(), AdviserPush.class);
        }
        return adviserPush;
    }

    /**
     * 删除推送
     *
     * @param pushId
     * @return void
     * @author 郑朋
     * @create 2018/3/20
     */
    public void deleteAdviserPush(Integer pushId) {
        pyRedisTemplate.opsForHash().delete(CacheKey.INVESTMENT_CONSIGNMENT_PUSH.getKey(), getPushKey(pushId));
    }
}
