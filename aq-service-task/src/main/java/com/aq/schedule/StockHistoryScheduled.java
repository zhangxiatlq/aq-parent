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
 * @ClassName: StockHistoryScheduled
 * @Description: 保存股票历史数据定时任务
 * @author: lijie
 * @date: 2018年3月7日 下午5:23:00
 */
@JobHandler(value = "stockHistoryScheduled")
@Component
@Slf4j
public class StockHistoryScheduled extends IJobHandler {
	
	@Reference(version = "1.0.0", check = false)
	private IToolCacheService toolCacheService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("保存股票历史数据定时任务 start = {}", param);
		try {
			Result result = toolCacheService.saveStockHistoryDatas();
			log.info("保存股票历史数据定时任务 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("执行保存股票历史数据定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}

}
