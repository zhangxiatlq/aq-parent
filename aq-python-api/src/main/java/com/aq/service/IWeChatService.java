package com.aq.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aq.facade.dto.WeChatBindDTO;
import com.aq.util.result.Result;
/**
 * 
 * @ClassName: IWeChatService
 * @Description: 微信相关服务
 * @author: lijie
 * @date: 2018年3月14日 上午10:52:32
 */
public interface IWeChatService {
	/**
	 * 
	* @Title: bindOpenId  
	* @Description: 绑定微信openId 
	* @param: @param dto
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result bindOpenId(WeChatBindDTO dto);
	
	/**
	 * 
	* @Title: processEvent  
	* @Description: 处理微信事件通知 
	* @param: @param request
	* @return void
	* @author lijie
	* @throws
	 */
	void processEvent(HttpServletRequest request,HttpServletResponse response);
}
