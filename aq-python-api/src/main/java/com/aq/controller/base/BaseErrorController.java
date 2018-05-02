package com.aq.controller.base;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.aq.core.exception.BizException;
import com.aq.util.result.RespCode;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class BaseErrorController {
	
	/**
	 * @Title: pageNoFoundHandler
	 * @Description: 404异常处理
	 * @param: @param request
	 * @param: @param exception
	 * @param: @param model
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	@ExceptionHandler(value = NoHandlerFoundException.class)
	public String pageNoFoundHandler(HttpServletRequest request, NoHandlerFoundException exception, Model model) {
		log.error("BaseErrorController NoHandlerFoundException >> ", exception);
		log.error("request error url >> " + request.getRequestURL());
		model.addAttribute("code", 404);
		model.addAttribute("msg", "您访问的页面暂时找不到了，有可能链接错误或者网络问题");
		return "error/404";
	}

	/**
	 * biz业务异常处理
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = BizException.class)
	@ResponseBody
	public Object bizJsonExceptionHandler(BizException e) {
		log.error("数据服务端接口异常", e);
		return ResultUtil.getResult(RespCode.Code.APP_ERROR);
	}
	/**
	 * 
	* @Title: exceptionHandler 
	* @Description:  
	* @param @param e
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws
	 */
	@ExceptionHandler(value = Throwable.class)
	@ResponseBody
	public Object exceptionHandler(Throwable e) {
		log.error("App接口异常", e);
		return ResultUtil.getResult(RespCode.Code.APP_ERROR);
	}
}
