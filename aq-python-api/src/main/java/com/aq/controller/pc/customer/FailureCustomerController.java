package com.aq.controller.pc.customer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.facade.dto.customer.DeleteImportDTO;
import com.aq.facade.service.customer.IFailureCustomerService;
import com.aq.facade.vo.customer.ImportCustomerVO;
import com.aq.facade.vo.customer.ImportFailureRecordVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.service.FailureCustomerService;
import com.aq.util.excel.ExcelBean;
import com.aq.util.excel.ExcelUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 导入失败的客户controller
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@RestController
@RequestMapping("/api/pc/failures")
@Api(value = "导入失败的客户接口", description = "导入失败的客户接口")
@Slf4j
public class FailureCustomerController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IFailureCustomerService failureCustomerService;

    @Autowired
    private FailureCustomerService failureService;

    @ApiOperation(value = "查询导入失败的客户列表", notes = "[郑朋]查询导入失败的客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ImportCustomerVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getFailureCustomerList(HttpServletRequest request, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        Integer managerId = manageInfoVO.getId();
        log.info("根据条件查询导入失败的客户入参：pageBean={},managerId={}", JSON.toJSONString(pageBean), managerId);
        Result result = failureCustomerService.getFailureCustomerByPage(pageBean, managerId);
        log.info("根据条件查询导入失败的客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "查询导入失败的客户列表", notes = "[郑朋]查询导入失败的客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ImportCustomerVO.class)
    })
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @AccessToken
    public void exportFailureCustomerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        Integer managerId = manageInfoVO.getId();
        log.info("导出-导入失败的客户入参：managerId={}", managerId);
        Result result = failureCustomerService.getFailureCustomerByList(managerId);
        log.info("导出-导入失败的客户返回值：result={}", JSON.toJSONString(result));
        ExcelBean excelBean = failureService.excelBean((List<ImportFailureRecordVO>) result.getData());
        ExcelUtil.download(request, response, excelBean);
    }

    @ApiOperation(value = "删除导入失败的客户", notes = "[郑朋]删除导入失败的客户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "list", value = "删除导入失败的客户集合", paramType = "query", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/failure", method = RequestMethod.DELETE)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result deleteFailureCustomer(HttpServletRequest request, DeleteImportDTO deleteImportDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        deleteImportDTO.setManagerId(manageInfoVO.getId());
        log.info("删除导入失败的客户入参：deleteImportDTO={}", JSON.toJSONString(deleteImportDTO));
        Result result = failureCustomerService.deleteFailureCustomer(deleteImportDTO);
        log.info("删除导入失败的客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
