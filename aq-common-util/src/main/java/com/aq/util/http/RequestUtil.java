package com.aq.util.http;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: RequestUtil
 * @Description: 请求处理工具类
 * @author: lijie
 * @date: 2018年2月8日 下午4:11:44
 */
@Slf4j
public class RequestUtil {

	private static final String OPENID = "OPENID";
	/**
	 * 
	 * @Title: getOpenId
	 * @author: lijie 
	 * @Description: 根据请求获取header里的openId
	 * @param request
	 * @return
	 * @return: String
	 */
	public static String getOpenId(HttpServletRequest request) {
		String openId = request.getHeader(OPENID);
		log.info("请求header获取openId数据={}", openId);
		return openId;
	}
}
