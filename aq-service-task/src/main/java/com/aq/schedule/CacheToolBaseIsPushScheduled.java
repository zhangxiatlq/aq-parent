package com.aq.schedule;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.IToolCacheService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: CacheToolBaseIsPushScheduled
 * @Description: 修改缓存里的工具数据是否推送
 * @author: lijie
 * @date: 2018年3月9日 上午11:24:51
 */
@JobHandler(value = "cacheToolBaseIsPushScheduled")
@Component
@Slf4j
public class CacheToolBaseIsPushScheduled extends IJobHandler {

	@Reference(version = "1.0.0", check = false)
	private IToolCacheService toolCacheService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("修改缓存里的工具数据是否推送定时任务 start = {}", param);
		try {
			Result result = toolCacheService.updateCacheToolBaseIsPush();
			log.info("修改缓存里的工具数据是否推送 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("修改缓存里的工具数据是否推送定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}

}
