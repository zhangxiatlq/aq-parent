package com.aq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

/**
 * 调度任务模块入口
 * @author Mr.Chang
 */
@MapperScan(basePackages = "com.aq.*.mapper")
@SpringBootApplication
public class AqServiceTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AqServiceTaskApplication.class, args);
	}

	/**
	 * 设置tomcat端口号
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerFactory servletContainer(){
		TomcatEmbeddedServletContainerFactory factory=new TomcatEmbeddedServletContainerFactory();
		factory.setPort(20003);
		return factory;
	}
}
