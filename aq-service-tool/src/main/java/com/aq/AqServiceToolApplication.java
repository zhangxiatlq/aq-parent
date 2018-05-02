package com.aq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import com.aq.core.application.RedisSubscribeApplication;
import com.aq.service.impl.ToolSubscribeImpl;

@SpringBootApplication
@MapperScan(basePackages = "com.aq.mapper")
public class AqServiceToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(AqServiceToolApplication.class, args);
		RedisSubscribeApplication.run(ToolSubscribeImpl.class);
	}

	/**
	 * 设置tomcat端口号
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerFactory servletContainer(){
		TomcatEmbeddedServletContainerFactory factory=new TomcatEmbeddedServletContainerFactory();
		factory.setPort(20002);
		return factory;
	}
}
