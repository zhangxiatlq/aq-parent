package com.aq.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * dubbo消费端配置
 * @create 2018/1/17
 * @author Mr.Chang
 **/
@Configuration
@DubboComponentScan
@PropertySource(value = "classpath:dubbo-consumer.properties")
@Data
public class DubboConsumerConfig {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Value("${dubbo.registry.protocol}")
    private String protocol;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

    @Value("${dubbo.consumer.timeout}")
    private int timeout;

    @Value("${dubbo.consumer.retries}")
    private int retries;
    /**
     * @methodname applicationConfig 的描述：注入dubbo上下文
     * @create 2018/1/17
     * @author Mr.Chang
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(this.applicationName);
        applicationConfig.setLogger("slf4j");
        return applicationConfig;
    }

    /**
     * consumer config 注入
     *
     * @param
     * @return com.alibaba.dubbo.config.ConsumerConfig
     * @author 郑朋
     * @create 2018/2/22
     */
    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(this.timeout);
        // 设置启动时不检查注册中心
        consumerConfig.setCheck(false);
        return consumerConfig;
    }


    /**
     *
     * @methodname registryConfig 的描述：注入dubbo注册中心配置,基于zookeeper
     * @create 2018/1/17
     * @author Mr.Chang
     */
    @Bean
    public RegistryConfig registryConfig() {
        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol(protocol);
        registry.setAddress(registryAddress);
        return registry;
    }

}
