package com.aq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @项目：phshopping-parent
 * @描述：swagger配置
 * @作者： Mr.chang
 * @创建时间：2017/7/24
 * @Copyright @2017 by Mr.chang
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aq.controller"))
                .paths(PathSelectors.regex("/api/.*"))
                .build();
    }
    private ApiInfo apiInfo() {
        Contact contact=new Contact("Mr.Chang","www.aq.com","511098425@qq.com");
        return new ApiInfoBuilder()
                .title("APP接口文档")
                .description("APP接口文档")
                .contact(contact)
                .version("2.0")
                .license("AQ")
                .licenseUrl("https://git.oschina.net/MrZhang/aq-parent")
                .build();
    }
}
