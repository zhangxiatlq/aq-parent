package com.aq.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * dubbo消费端配置
 *
 * @author Mr.Chang
 * @create 2018/1/17
 **/
@Configuration
@DubboComponentScan(basePackages = "com.aq")
@PropertySource(value = "classpath:dubbo-consumer.properties")
@Data
public class DubboConsumer {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Value("${dubbo.registry.protocol}")
    private String protocol;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

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
