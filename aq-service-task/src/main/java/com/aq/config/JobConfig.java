package com.aq.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.chang
 * @项目：cloud-parent
 * @描述：
 * @创建时间：2017/12/28
 * @Copyright @2017 by Mr.chang
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.aq.schedule")
public class JobConfig {

    @Autowired
    private JobProperties jobProperties;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        log.info("获取的参数："+jobProperties.getIp()+"注册地址："+jobProperties.getAddresses());
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setIp(jobProperties.getIp());
        xxlJobExecutor.setPort(jobProperties.getPort());
        xxlJobExecutor.setAppName(jobProperties.getAppname());
        xxlJobExecutor.setAdminAddresses(jobProperties.getAddresses());
        xxlJobExecutor.setLogPath(jobProperties.getLogpath());
        xxlJobExecutor.setAccessToken(jobProperties.getAccessToken());
        return xxlJobExecutor;
    }

}
