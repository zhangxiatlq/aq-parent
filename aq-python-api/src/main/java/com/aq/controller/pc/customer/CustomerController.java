package com.aq.controller.pc.customer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.facade.dto.customer.*;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.vo.customer.ImportCustomerVO;
import com.aq.facade.vo.customer.QueryCustomerVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 客户controller
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@RestController
@RequestMapping("/api/pc/customers")
@Api(value = "客户管理接口", description = "客户管理接口")
@Slf4j
public class CustomerController extends BaseController {


    @Reference(version = "1.0.0", check = false,timeout = 1800000)
    private ICustomerService customerService;

    @ApiOperation(value = "查询我的客户列表", notes = "[郑朋]查询我的客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = QueryCustomerVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getCustomerList(HttpServletRequest request, PageBean pageBean, QueryCustomerDTO queryCustomerDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        queryCustomerDTO.setManagerId(manageInfoVO.getId());
        log.info("根据条件查询我的客户入参：pageBean={},queryCustomerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(queryCustomerDTO));
        Result result = customerService.getCustomerByPage(pageBean, queryCustomerDTO);
        log.info("根据条件查询我的客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "新增客户", notes = "[郑朋]新增客户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result addCustomer(HttpServletRequest request, BindCustomerDTO bindCustomerDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        bindCustomerDTO.setManagerId(manageInfoVO.getId());
        log.info("新增客户入参：bindCustomerDTO={}", JSON.toJSONString(bindCustomerDTO));
        Result result = customerService.bindCustomer(bindCustomerDTO);
        log.info("新增客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "修改客户", notes = "[郑朋]修改客户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/customer", method = RequestMethod.PUT)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result updateCustomer(HttpServletRequest request, UpdateBindCustomerDTO updateBindCustomerDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        updateBindCustomerDTO.setManagerId(manageInfoVO.getId());
        log.info("修改客户入参：updateBindCustomerDTO={}", JSON.toJSONString(updateBindCustomerDTO));
        Result result = customerService.updateBindCustomer(updateBindCustomerDTO);
        log.info("修改客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "导入客户", notes = "[郑朋]导入客户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "导入数据", paramType = "query", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ImportCustomerVO.class)
    })
    @RequestMapping(value = "/customer/import", method = RequestMethod.POST)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result importCustomer(HttpServletRequest request, String list) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            log.info("导入客户参数，list={}", list);
            ImportCustomerDTO importCustomerDTO = new ImportCustomerDTO();
            List<CustomerDTO> customerDTOS = JSON.parseArray(list, CustomerDTO.class);
            importCustomerDTO.setList(customerDTOS);
            importCustomerDTO.setManagerId(manageInfoVO.getId());
            log.info("导入客户入参：importCustomerDTO={}", JSON.toJSONString(importCustomerDTO));
            result = customerService.importCustomer(importCustomerDTO);
        } catch (Exception e) {
            log.info("导入客户异常, e={}", e);
        }
        log.info("导入客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }


}
