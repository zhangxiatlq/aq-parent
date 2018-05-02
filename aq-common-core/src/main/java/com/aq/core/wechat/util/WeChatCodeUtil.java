package com.aq.core.wechat.util;

import org.apache.commons.codec.binary.Base64;
/**
 * 
 * @ClassName: CodeUtil
 * @Description: 编码工具
 * @author: lijie
 * @date: 2018年3月1日 下午4:37:59
 */
public class WeChatCodeUtil {

	/**
	 * 
	* @Title: encode  
	* @Description: 编码参数  
	* @param @param data
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static String encode(String data) {

		return Base64.encodeBase64String(data.getBytes());
	}
	/**
	 * 
	* @Title: decode  
	* @Description: 解码回调数据  
	* @param @param data
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	public static String decode(String data) {

		return new String(Base64.decodeBase64(data));
	}
}
