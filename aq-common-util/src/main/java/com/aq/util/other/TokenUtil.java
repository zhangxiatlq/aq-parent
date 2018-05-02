package com.aq.util.other;

import java.util.UUID;

/**
 * 
 * @ClassName: TokenUtil
 * @Description: token工具类
 * @author: lijie
 * @date: 2018年2月11日 下午3:28:28
 */
public class TokenUtil {

	
	/**
	 * 
	 * @Title: getKey
	 * @author: lijie 
	 * @Description: 根据手机号生成key
	 * @param telphone
	 * @return
	 * @return: String
	 */
	public static String getManagerKey(String telphone) {

		return "MANAGER_" + telphone;
	}
	/**
	 * 
	* @Title: generateToken  
	* @Description: 生成token
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static String generateToken(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
