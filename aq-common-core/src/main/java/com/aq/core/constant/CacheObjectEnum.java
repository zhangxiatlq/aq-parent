package com.aq.core.constant;
/**
 * 
* @ClassName: CacheObjectEnum 
* @Description: 缓存对象类型（针对redis） 
* @author lijie
* @date 2017年8月6日 下午6:08:49 
*
 */
public enum CacheObjectEnum {
	/**
	 * 字符串对象
	 */
	STRING,
	/**
	 * 字典对象
	 */
	HASH,
	/**
	 * 列表对象
	 */
	LIST,
	/**
	 * 集合对象
	 */
	SET,
	/**
	 * 有序集合对象
	 */
	ZSET;
}
