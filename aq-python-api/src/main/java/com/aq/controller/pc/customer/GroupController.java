package com.aq.controller.pc.customer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.facade.dto.customer.AddOrUpdateGroupDTO;
import com.aq.facade.dto.customer.DeleteGroupDTO;
import com.aq.facade.dto.customer.QueryGroupDTO;
import com.aq.facade.service.customer.IGroupService;
import com.aq.facade.vo.customer.QueryGroupVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 分组controller
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@RestController
@RequestMapping("/api/pc/groups")
@Api(value = "分组接口", description = "分组接口")
@Slf4j
public class GroupController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IGroupService groupService;


    @ApiOperation(value = "查询分组列表", notes = "[郑朋]查询分组列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = QueryGroupVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getGroupList(HttpServletRequest request, QueryGroupDTO queryGroupDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        queryGroupDTO.setManagerId(manageInfoVO.getId());
        log.info("根据条件查询分组入参：queryGroupDTO={}", JSON.toJSONString(queryGroupDTO));
        Result result = groupService.getGroupList(queryGroupDTO);
        log.info("根据条件查询分组返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "新增分组", notes = "[郑朋]新增分组接口")
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    @AccessToken
    public Result addGroup(HttpServletRequest request, AddOrUpdateGroupDTO addOrUpdateGroupDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        addOrUpdateGroupDTO.setManagerId(manageInfoVO.getId());
        log.info("新增分组入参：addOrUpdateGroupDTO={}", JSON.toJSONString(addOrUpdateGroupDTO));
        Result result = groupService.addGroup(addOrUpdateGroupDTO);
        log.info("新增分组返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "修改分组", notes = "[郑朋]修改分组接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result updateGroup(HttpServletRequest request, AddOrUpdateGroupDTO addOrUpdateGroupDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        addOrUpdateGroupDTO.setManagerId(manageInfoVO.getId());
        log.info("修改分组入参：addOrUpdateGroupDTO={}", JSON.toJSONString(addOrUpdateGroupDTO));
        Result result = groupService.updateGroup(addOrUpdateGroupDTO);
        log.info("修改分组返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "删除分组", notes = "[郑朋]删除分组接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "/group", method = RequestMethod.DELETE)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result deleteGroup(HttpServletRequest request, DeleteGroupDTO deleteGroupDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        deleteGroupDTO.setManagerId(manageInfoVO.getId());
        log.info("删除分组入参：deleteGroupDTO={}", JSON.toJSONString(deleteGroupDTO));
        Result result = groupService.deleteGroup(deleteGroupDTO);
        log.info("删除分组返回值：result={}", JSON.toJSONString(result));
        return result;
    }


}
