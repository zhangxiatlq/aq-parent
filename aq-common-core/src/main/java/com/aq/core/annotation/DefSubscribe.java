package com.aq.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aq.core.constant.ChannelEnum;
import com.aq.core.rediscache.listener.ISubscribeListener;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DefSubscribe {
	/**
	 * 
	* @Title: channel 
	* @Description: 频道名称 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author lijie
	* @throws
	 */
	ChannelEnum channel();
	/**
	 * 
	* @Title: listener 
	* @Description: 频道监听事件
	* @param @return    设定文件 
	* @return Class<? extends ISubscribeListener>[]    返回类型 
	* @author lijie
	* @throws
	 */
	Class<? extends ISubscribeListener>[] listener();
}
