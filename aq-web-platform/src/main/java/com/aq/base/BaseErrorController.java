package com.aq.base;


import com.aq.core.exception.BizException;
import com.aq.util.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;


/**
 * 通用异常处理
 *
 * @author 郑朋
 * @create 2018/1/22
 */
@ControllerAdvice
@Slf4j
public class BaseErrorController {


    /**
     * 500异常处理
     *
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error("异常信息,e={}", e);
        ModelAndView view = new ModelAndView();
        view.addObject("code", 500);
        view.addObject("msg", e.getMessage());
        view.addObject("url", request.getRequestURL());
        view.setViewName("error/500");
        return view;
    }

    /**
     * 404异常处理
     *
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ModelAndView pageNoFoundHandler(HttpServletRequest request, Exception e) {
        log.error("异常信息,e={}", e);
        ModelAndView view = new ModelAndView();
        view.addObject("code", 404);
        view.addObject("msg", "页面未找到");
        view.addObject("url", request.getRequestURL());
        view.setViewName("error/404");
        return view;
    }

    /**
     * biz业务异常处理
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result bizExceptionHandler(BizException e) {
        log.error("异常信息, msg={},code={},e={}", e.getMsg(), e.getCode(), e.getStackTrace());
        return new Result(false, e.getCode(), e.getMsg());
    }

}
