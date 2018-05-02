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
 * @describe：定时（每天0点半）更新客户关注状态
 * @author： 张霞
 * @createTime： 2018/03/06
 * @Copyright @2017 by zhangxia
 */
@JobHandler(value = "customerAttentionUpdateScheduled")
@Component
@Slf4j
public class CustomerUpdateAttentionWechatScheduled extends IJobHandler{

    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;


    @Override
    public ReturnT<String> execute(String s) throws Exception {

        log.info("定时更新客户关注状态开始时间={}", JSON.toJSONString(new Date()));
              customerService.updateCustomerAttention();
        log.info("定时更新客户关注状态结束时间={}", JSON.toJSONString(new Date()));
        return SUCCESS;
    }
}
