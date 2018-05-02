package com.aq.schedule;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: CustomerVipScheduled
 * @Description: 客户vip定时任务
 * @author: lijie
 * @date: 2018年2月24日 上午11:30:11
 */
@JobHandler(value = "customerVipScheduled")
@Component
@Slf4j
public class CustomerVipScheduled extends IJobHandler {

	
	@Reference(version = "1.0.0", check = false)
	private ICustomerService customerService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("客户vip到期定时任务 start = {}", param);
		try {
			Result result = customerService.timingUpdateCustomerVip();
			log.info("客户vip到期定时任务 返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("客户vip到期定时任务异常", e);
			return FAIL;
		}
		return SUCCESS;
	}
}
