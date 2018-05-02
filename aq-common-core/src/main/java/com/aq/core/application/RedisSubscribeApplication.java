package com.aq.core.application;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.Assert;

import com.aq.core.annotation.DefSubscribe;
import com.aq.core.rediscache.listener.ISubscribeListener;
import com.aq.util.bean.SpringUtil;

import lombok.Data;

/**
 * 
 * @ClassName: RedisSubscribeApplication
 * @Description: redis 订阅初始化 
 * @author: lijie
 * @date: 2018年2月27日 下午3:16:56
 */
public class RedisSubscribeApplication {

	private static final RedisMessageListenerContainer CONTAINER = SpringUtil
			.getBeanByClass(RedisMessageListenerContainer.class);

	private final List<Object> sources = new LinkedList<Object>();

	private RedisSubscribeApplication(List<Object> sources) {
		initialize(sources);
	}

	private void initialize(List<Object> sources) {
		if (sources != null && !sources.isEmpty()) {
			this.sources.addAll(sources);
		}
		// init end
	}
	/**
	 * 
	* @Title: run 
	* @Description: 订阅通道启动 
	* @param @param clas    设定文件 
	* @return void    返回类型 
	* @author lijie
	* @throws
	 */
	public static void run(Class<?> clas) {
		Assert.notNull(clas, "run redis subscribe Class is null");
		List<Object> sources = new LinkedList<Object>();
		DefSubscribe sub = clas.getAnnotation(DefSubscribe.class);
		if (null != sub) {
			sources.add(sub);
		}
		Method[] methods = clas.getMethods();
		if (null != methods) {
			int len = methods.length;
			for (int i = 0; i < len; i++) {
				sub = methods[i].getAnnotation(DefSubscribe.class);
				if (null != sub) {
					sources.add(sub);
				}
			}
		}
		new RedisSubscribeApplication(sources).start();
	}
	/**
	 * 
	* @ClassName: SubscribeMessageConfig 
	* @Description: 订阅频道初始化 
	* @author lijie
	* @date 2017年11月28日 下午5:29:42 
	*
	 */
	@Data
	public static class SubscribeMessageConfig {
		private String channelName;
		private List<? extends ISubscribeListener> subListener;
	}
	/**
	 * 
	* @Title: init 
	* @Description: 初始化订阅信息
	* @param @param config
	* @param @param clas    设定文件 
	* @return void    返回类型 
	* @author lijie
	* @throws
	 */
	private void start() {
		if (!sources.isEmpty()) {
			final List<SubscribeMessageConfig> mcs = new ArrayList<>(16);
			SubscribeMessageConfig sm;
			for (Object sour : sources) {
				if (sour instanceof DefSubscribe) {
					sm = handle((DefSubscribe) sour);
					if (null != sm) {
						mcs.add(sm);
					}
				}
			}
			if (!mcs.isEmpty()) {
				ChannelTopic ct;
				List<? extends ISubscribeListener> subClas;
				for (SubscribeMessageConfig mc : mcs) {
					if (StringUtils.isBlank(mc.getChannelName()) || null == mc.getSubListener()) {
						continue;
					}
					ct = new ChannelTopic(mc.getChannelName());
					subClas = mc.getSubListener();
					for (int i = 0; i < subClas.size(); i++) {
						CONTAINER.addMessageListener(subClas.get(i), ct);
					}
				}
			}
		}
	}

	private SubscribeMessageConfig handle(final DefSubscribe sub) {
		SubscribeMessageConfig sm = new SubscribeMessageConfig();
		sm.setChannelName(sub.channel().getChannelName());
		final List<ISubscribeListener> subListener = new ArrayList<>(16);
		Class<? extends ISubscribeListener>[] ls = sub.listener();
		ISubscribeListener sl;
		for (Class<? extends ISubscribeListener> clas : ls) {
			sl = SpringUtil.getBeanByClass(clas);
			if (null != sl) {
				subListener.add(sl);
			}
		}
		sm.setSubListener(subListener);
		return sm;
	}
}
