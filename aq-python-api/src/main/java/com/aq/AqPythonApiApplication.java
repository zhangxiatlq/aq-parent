package com.aq;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@DubboComponentScan(basePackages = "com.aq.facade.service")
public class AqPythonApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AqPythonApiApplication.class, args);
	}
}
