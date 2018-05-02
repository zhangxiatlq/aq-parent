package com.aq.schedule;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.IToolService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: SynchroTrendToolToRedis
 * @Description: 同步数据到redis
 * @author: lijie
 * @date: 2018年3月7日 下午5:57:06
 */
@JobHandler(value = "synchroTrendToolToRedis")
@Component
@Slf4j
@Deprecated
public class SynchroTrendToolToRedis extends IJobHandler {
	
	@Reference(version = "1.0.0", check = false)
	private IToolService toolService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("同步绑定趋势量化工具数据到redis任务 start = {}", param);
		try {
			Result result = toolService.synchroTrendToolToRedis();
			log.info("同步绑定趋势量化工具数据到redis任务 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("执行同步绑定趋势量化工具数据到redis定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}

}
