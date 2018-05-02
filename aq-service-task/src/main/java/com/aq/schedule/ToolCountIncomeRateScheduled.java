package com.aq.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.facade.service.IToolService;
import com.aq.util.result.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @author:zhangxia
 * @createTime: 2018/4/26 18:37
 * @version:1.0
 * @desc: 定时计算专业趋势量化工具的收益率
 */
@JobHandler(value = "toolCountIncomeRateScheduled")
@Component
@Slf4j
public class ToolCountIncomeRateScheduled extends IJobHandler{

    @Reference(version = "1.0.0",check = false)
    private IToolService toolService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("定时计算专业趋势量化工具的收益率 start = {}",s);
        try {
            Result result = toolService.timingCountToolIncomeRate(Boolean.valueOf(s));
        } catch (ParseException e) {
            log.error("执行 tool定时计算专业趋势量化工具的收益率 任务异常,e={}",e);
            return FAIL;
        }
        return SUCCESS;
    }
}
