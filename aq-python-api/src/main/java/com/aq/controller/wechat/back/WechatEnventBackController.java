package com.aq.controller.wechat.back;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.service.IWeChatService;
import com.aq.util.wechat.util.SignUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * describe:
 *
 * @author zhangxia
 * @version 2.0
 * @date 2018/03/05
 */
@Slf4j
@Controller
@RequestMapping("wechat")
public class WechatEnventBackController {
	
	@Autowired
	private IWeChatService weChatService;
	
	@Autowired
	private WeChatCoreProperties wechatProperties;
	/**
	 * 
	* @Title: processRequest  
	* @Description: 微信服务事件通知配置/事件通知 
	* @param: @param request
	* @param: @param response
	* @param: @throws IOException
	* @return void
	* @author lijie
	* @throws
	 */
	@RequestMapping(value = "/event")
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("微信服务事件通知配置/事件通知 start");
		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			log.info("signature:" + signature);
			log.info("timestamp:" + timestamp);
			log.info("nonce:" + nonce);
			log.info("echostr:" + echostr);
			PrintWriter pw = response.getWriter();
			if (SignUtil.checkSignature(wechatProperties.getToken(), signature, timestamp, nonce)) {
				pw.append(echostr);
			}
			pw.flush();
			// 释放资源
			pw.close();
		} else {
			weChatService.processEvent(request, response);
		}
	}
}
