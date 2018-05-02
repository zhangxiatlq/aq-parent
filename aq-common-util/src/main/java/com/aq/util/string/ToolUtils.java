package com.aq.util.string;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: ToolUtils
 * @Description: 工具相关工具类
 * @author: lijie
 * @date: 2018年2月12日 下午1:37:11
 */
public class ToolUtils {
	/**
	 * 工具同步异常常量值
	 */
	public static final String TOOL_SYNCHRO_ERROR = "TOOL_SYNCHRO_ERROR";
	
	public static final String TOOL = "TOOL_";
	
	public static final String TOOLBIND = "TOOL_BIND_";
	/**
	 * 
	* @Title: getToolKey  
	* @Description: 根据工具ID得到key  
	* @param @param id
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static String getToolKey(Integer id) {

		return TOOL + id;
	}
	
	/**
	 * 
	* @Title: getToolBindKey  
	* @Description: 绑定 标识  
	* @param @param id
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static String getToolBindKey(Integer id) {

		return TOOLBIND + id;
	}
	/**
	 * 
	* @Title: getToolIdByTool  
	* @Description: 回去toolID  
	* @param @param toolIdenty
	* @param @return    参数  
	* @return Integer    返回类型  
	* @throws
	 */
	public static Integer getToolIdByTool(String toolIdenty) {
		if (StringUtils.isNotBlank(toolIdenty)) {
			try {
				return Integer.valueOf(toolIdenty.split(TOOL)[1]);
			} catch (Exception e) {
			}
		}
		return null;
	}
	/**
	/**
	 * 
	* @Title: getToolIdByBind  
	* @Description: 获取tool绑定ID 
	* @param @param toolIdenty
	* @param @return    参数  
	* @return Integer    返回类型  
	* @throws
	 */
	public static Integer getToolIdByBind(String toolIdenty) {
		if (StringUtils.isNotBlank(toolIdenty)) {
			try {
				return Integer.valueOf(toolIdenty.split(TOOLBIND)[1]);
			} catch (Exception e) {
			}
		}
		return null;
	}
}
