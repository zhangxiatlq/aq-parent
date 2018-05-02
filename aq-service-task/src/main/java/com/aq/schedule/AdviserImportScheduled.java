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
 * 导入定时更新
 *
 * @author 郑朋
 * @create 2018/4/12
 **/
@JobHandler(value = "adviserImportScheduled")
@Component
@Slf4j
public class AdviserImportScheduled extends IJobHandler {
    @Reference(version = "1.0.0")
    private IAdviserService adviserService;

    @Override
    public ReturnT<String> execute(String s) {
        log.info("导入定时更新 start = {}", System.currentTimeMillis());
        try {
            Result result = adviserService.modifyAdviserImportScheduled();
            log.info("导入定时更新 返回数据={}, endTime={}", JSON.toJSONString(result), System.currentTimeMillis());
        } catch (Exception e) {
            log.error("导入定时更新 e={}", e);
            return FAIL_RETRY;
        }
        return SUCCESS;
    }
}