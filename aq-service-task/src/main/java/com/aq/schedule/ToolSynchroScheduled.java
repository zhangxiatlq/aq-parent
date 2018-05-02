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
 * @ClassName: ToolSynchroScheduled
 * @Description: 工具同步定时任务
 * @author: lijie
 * @date: 2018年2月24日 上午11:00:10
 */
@JobHandler(value = "toolSynchroScheduled")
@Component
@Slf4j
public class ToolSynchroScheduled extends IJobHandler {
	
	@Reference(version = "1.0.0", check = false)
	private IToolService toolService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("tool同步定时任务 start = {}", param);
		try {
			Result result = toolService.timingUpdateToolSynchro();
			log.info("tool同步定时任务 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("执行tool同步定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}
	
}
