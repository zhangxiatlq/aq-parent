package com.aq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 
 * @ClassName: PythonConfig
 * @Description: python同步配置
 * @author: lijie
 * @date: 2018年2月12日 下午7:11:01
 */
@PropertySource("classpath:python.properties")
@Component
@ConfigurationProperties(prefix = "python")
@Data
public class PythonConfig {
	/**
	 * 工具地址
	 */
	private String url;
	/**
	 * 触发地址
	 */
	private String trigger;
	/**
	 * 
	* @Title: getAddGridUrl  
	* @Description: 添加网格工具地址
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getAddGridUrl() {

		return url + "addGridQAtoolStockCode";
	}
	/**
	 * 
	* @Title: getAddSellingUrl  
	* @Description: 添加卖点工具地址 
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getAddSellingUrl() {

		return url + "addSellingQAtoolStockCode";
	}
	/**
	 * 
	* @Title: getAddTrendUrl  
	* @Description: 添加趋势化工具地址  
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	@Deprecated
	public String getAddTrendUrl() {

		return url + "addTrendAQToolStockcode";
	}
	/**
	 * 
	* @Title: deleteGridUrl  
	* @Description: 删除网格工具  
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getDeleteGridUrl(){
		return url + "delGridQAtoolStockCode";
	}
	/**
	 * 
	* @Title: getDeleteSellingUrl  
	* @Description: 删除卖点工具url  
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getDeleteSellingUrl(){
		return url + "delSellingQAtoolStockCode";
	}
	/**
	 * 
	* @Title: getDeleteSellingUrl  
	* @Description: 删除趋势化工具url  
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getDeleteTrendUrl(){
		return url + "delTrendAQToolStockcode";
	}
	/**
	 * 
	* @Title: getStockInfoUrl  
	* @Description: 获取股票详情信息url  
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getStockInfoUrl(){
		return url + "getCodeStockName";
	}
	/**
	 * 
	* @Title: getAddTrendQOneItem  
	* @Description: 添加趋势化工具地址--普通
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public String getAddTrendQOneItem(){
		return trigger + "addTrendQOneItem";
	}

	/**
	 * 添加趋势化工具地址--专用
	 *
	 * @param
	 * @return java.lang.String
	 * @author 郑朋
	 * @create 2018/4/25
	 */
	public String getAddTrendQAtoolSpecific(){
		return trigger + "addTrendQAtoolSpecific";
	}
}
