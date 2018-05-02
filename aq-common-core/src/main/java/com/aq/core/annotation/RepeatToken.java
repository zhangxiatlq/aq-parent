package com.aq.core.annotation;

import java.lang.annotation.*;

/**
 * 
* @ClassName: RequestToken 
* @Description: 请求控制token（主要用于防止重复提交） 
* @author lijie
* @date 2017年8月6日 下午4:14:28 
*
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatToken {
	/**
	 * 
	* @Title: key 
	* @Description: 指定校验key 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	String key() default "";
	/**
	 * 
	* @Title: isHeader 
	* @Description: 需要获取的key值是否在请求头部
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	boolean isHeader() default false;
	/**
	 * 
	* @Title: headerKey 
	* @Description: 在header中获取的key name 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	String headerKey() default "";
}
