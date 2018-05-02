package com.aq.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.IAdviserService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 每天委托撤单
 *
 * @author 郑朋
 * @create 2018/3/8 0008
 **/
@JobHandler(value = "adviserPushScheduled")
@Component
@Slf4j
public class AdviserPushScheduled extends IJobHandler {

    @Reference(version = "1.0.0")
    private IAdviserService adviserService;

    @Override
    public ReturnT<String> execute(String s) {
        log.info("每天委托撤单 start = {}", System.currentTimeMillis());
        try {
            Result result = adviserService.modifyAdviserPushScheduled();
            log.info("每天委托撤单 返回数据={}, endTime={}", JSON.toJSONString(result), System.currentTimeMillis());
        } catch (Exception e) {
            log.error("每天委托撤单 异常, e={}", e);
            return FAIL_RETRY;
        }
        return SUCCESS;
    }
}
