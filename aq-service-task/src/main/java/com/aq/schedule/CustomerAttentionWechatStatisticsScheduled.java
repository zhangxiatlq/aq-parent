package com.aq.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.customer.ICustomerService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：定时（每天1点）记录每天每个客户经理下面的客户关注微信的数量
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@JobHandler(value = "customerAttentionWechatStatisticsScheduled")
@Component
@Slf4j
public class CustomerAttentionWechatStatisticsScheduled extends IJobHandler{


    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("记录每天每个客户经理下面的客户关注微信的数量定时器开始执行时间={}", JSON.toJSONString(new Date()));
        customerService.statisticsAttentionRecord();
        log.info("记录每天每个客户经理下面的客户关注微信的数量定时器结束执行时间={}", JSON.toJSONString(new Date()));
        return SUCCESS;
    }
}
