package com.aq.controller.wechat.consult;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.facade.service.IConsultService;
import com.aq.facade.vo.ConsultManagerVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.util.http.RequestUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 今日汇 controller
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
@Slf4j
@RestController
@RequestMapping(value = "api/wechat/consult")
@CrossOrigin(origins = "*")
@Api(value = "今日汇接口", description = "今日汇接口")
public class ConsultWeChatController extends BaseController {

    @Reference(version = "1.0.0")
    private IConsultService consultService;

    @ApiOperation(value = "今日汇列表", notes = "[郑朋]今日汇列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "openId", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ConsultManagerVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public Result getConsult(PageBean pageBean, HttpServletRequest request) {
        CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
        log.info("今日汇列表入参：pageBean={},customerId={}", JSON.toJSONString(pageBean), customerInfoVO.getId());
        Result result = consultService.getWeChatConsult(pageBean, customerInfoVO.getId());
        log.info("今日汇列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "今日汇详情", notes = "[郑朋]今日汇详情")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "id", value = "今日汇id", paramType = "path", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ConsultManagerVO.class)
    })
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result getConsultById(@PathVariable Integer id, HttpServletRequest request) {
        CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
        log.info("今日汇详情入参：id={},customerId={}", id, customerInfoVO.getId());
        Result result = consultService.getWeChatConsultById(id, customerInfoVO.getId());
        log.info("今日汇详情返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
