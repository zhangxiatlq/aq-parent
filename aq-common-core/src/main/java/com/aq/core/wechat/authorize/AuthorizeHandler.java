package com.aq.core.wechat.authorize;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.util.WeChatCodeUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: AuthorizeHandler
 * @Description: 授权处理
 * @author: lijie
 * @date: 2018年3月1日 下午4:07:26
 */
@Slf4j
@Component
public class AuthorizeHandler {

	/**
	 * 
	* @Title: webAuthorize  
	* @Description: 网页授权  
	* @param @param response    参数  
	* @return void    返回类型  
	* @throws
	 */
	public void webAuthorize(HttpServletResponse response, String url, JSONObject json ){
		try {
			StringBuilder sbud = new StringBuilder(WeChatConfig.WEB_AUTHORIZE_URL);
			sbud.append("?appid=")
			.append(WeChatConfig.APPID)
			.append("&redirect_uri=")
			.append(URLEncoder.encode(url,"utf-8"))
			.append("&response_type=code")
			.append("&scope=snsapi_base&state=")
			.append(WeChatCodeUtil.encode(json.toJSONString()));
			response.sendRedirect(sbud.toString());
		} catch (IOException e) {
			log.info("微信网页授权重定向异常", e);
		}
	}
}
