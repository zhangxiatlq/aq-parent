package com.aq.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.IToolCacheService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 保存股票历史数据定时任务--专用趋势化
 *
 * @author 郑朋
 * @create 2018/4/26
 */
@JobHandler(value = "specialStockHistoryScheduled")
@Component
@Slf4j
public class SpecialStockHistoryScheduled extends IJobHandler {
	
	@Reference(version = "1.0.0", check = false)
	private IToolCacheService toolCacheService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("保存股票历史数据定时任务--专用趋势化 start = {}", param);
		try {
			Result result = toolCacheService.saveSpecialStockHistoryData();
			log.info("保存股票历史数据定时任务--专用趋势化 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("保存股票历史数据定时任务--专用趋势化 异常", e);
			return FAIL;
		}
		return SUCCESS;
	}

}
