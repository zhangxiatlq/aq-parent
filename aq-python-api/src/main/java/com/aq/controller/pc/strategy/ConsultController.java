package com.aq.controller.pc.strategy;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.facade.dto.ConsultAddDTO;
import com.aq.facade.dto.ConsultQueryDTO;
import com.aq.facade.dto.ConsultRemoveDTO;
import com.aq.facade.service.IConsultService;
import com.aq.facade.vo.ConsultQueryVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 今日汇 controller
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@RestController
@RequestMapping("/api/pc/consults")
@Api(value = "今日汇接口", description = "今日汇接口")
@Slf4j
public class ConsultController extends BaseController {


    @Reference(version = "1.0.0")
    private IConsultService consultService;

    @ApiOperation(value = "今日汇列表", notes = "[郑朋]今日汇列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ConsultQueryVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getConsult(ConsultQueryDTO consultQueryDTO, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        consultQueryDTO.setManagerId(manageInfoVO.getId());
        log.info("今日汇列表入参：pageBean={},consultQueryDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(consultQueryDTO));
        Result result = consultService.getConsultPage(pageBean, consultQueryDTO);
        log.info("今日汇列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "新增今日汇", notes = "[郑朋]新增今日汇")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "consult", method = RequestMethod.POST)
    @AccessToken
    public Result addConsult(ConsultAddDTO consultAddDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        consultAddDTO.setManagerId(manageInfoVO.getId());
        log.info("新增今日汇入参：consultAddDTO={}", JSON.toJSONString(consultAddDTO));
        Result result = consultService.addConsult(consultAddDTO);
        log.info("新增今日汇返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "删除今日汇", notes = "[郑朋]删除今日汇")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "consult", method = RequestMethod.DELETE)
    @AccessToken
    public Result removeConsult(ConsultRemoveDTO consultRemoveDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        consultRemoveDTO.setManagerId(manageInfoVO.getId());
        log.info("删除今日汇入参：consultRemoveDTO={}", JSON.toJSONString(consultRemoveDTO));
        Result result = consultService.removeConsult(consultRemoveDTO);
        log.info("删除今日汇返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
