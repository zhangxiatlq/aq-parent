package com.aq.schedule;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.ILuckDrawService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: PrizeScheduled
 * @Description: vip到期定时任务
 * @author: lijie
 * @date: 2018年1月26日 上午10:48:22
 */
@JobHandler(value = "prizeScheduled")
@Component
@Slf4j
public class PrizeScheduled extends IJobHandler {

	@Reference(version = "1.0.0", check = false)
	private ILuckDrawService luckDrawService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("vip到期定时任务 start = {}", param);
		try {
			Result result = luckDrawService.vipExpireGive();
			log.info("vip到期定时任务 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("执行vip到期定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}

}
