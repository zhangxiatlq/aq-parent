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
 * 删除持仓数量为0的数据
 *
 * @author 郑朋
 * @create 2018/3/16
 **/
@JobHandler(value = "adviserPositionHoldScheduled")
@Component
@Slf4j
public class AdviserPositionHoldScheduled extends IJobHandler {
    @Reference(version = "1.0.0")
    private IAdviserService adviserService;

    @Override
    public ReturnT<String> execute(String s) {
        log.info("删除持仓数量为0的数据 start = {}", System.currentTimeMillis());
        try {
            Result result = adviserService.deleteAdviserPosition();
            log.info("删除持仓数量为0的数据 返回数据={}, endTime={}", JSON.toJSONString(result), System.currentTimeMillis());
        } catch (Exception e) {
            log.error("删除持仓数量为0的数据 e={}", e);
            return FAIL_RETRY;
        }
        return SUCCESS;
    }
}
