package com.aq.controller.pc.tool;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.facade.contant.ToolTypeEnum;
import com.aq.facade.dto.*;
import com.aq.facade.dto.add.AddGridToolDTO;
import com.aq.facade.dto.add.AddSellingToolDTO;
import com.aq.facade.dto.add.AddTrendToolDTO;
import com.aq.facade.dto.customer.QueryCustomerDTO;
import com.aq.facade.dto.update.UpdateGridToolDTO;
import com.aq.facade.dto.update.UpdateSellingToolDTO;
import com.aq.facade.service.IToolCacheService;
import com.aq.facade.service.IToolService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.vo.ToolBindInfoVO;
import com.aq.facade.vo.ToolBindVO;
import com.aq.facade.vo.ToolCacheVO;
import com.aq.facade.vo.ToolVO;
import com.aq.facade.vo.customer.QueryCustomerVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.service.IToolImportService;
import com.aq.util.container.ClassUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.ToolUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PcController
 * @Description: 工具相关接口
 * @author: lijie
 * @date: 2018年1月21日 下午10:48:20
 */
@Slf4j
@RestController
@RequestMapping("api/tools")
@Api(value = "工具相关接口", description = "工具相关接口")
public class PcToolController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IToolService toolService;

    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;

    @Reference(version = "1.0.0", check = false)
    private IToolCacheService toolCacheService;

    @Autowired
    private IToolImportService toolImportService;

    /**
     * @param dto
     * @param pageBean
     * @return
     * @Title: list
     * @author: lijie
     * @Description: 查询工具列表
     * @return: Result
     */
    @ApiOperation(value = "查询工具列表（根据工具编码查询不同的工具）", notes = "[李杰]查询工具列表")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ToolVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
    })
    @GetMapping
    @AccessToken
    public Result list(QueryToolDTO dto, PageBean pageBean) {
        dto.setManagerId(getManageInfo(true).getId());
        Result result = toolService.getPcToolsByPage(dto, pageBean);
        log.info("查询工具列表返回数据result={}", JSON.toJSONString(result));
        @SuppressWarnings("unchecked")
        List<ToolVO> rList = (List<ToolVO>) result.getData();
        if (null != rList && !rList.isEmpty()) {
            for (ToolVO tv : rList) {
                ClassUtil.setDefvalue(tv, "userId");
            }
        }
        return result;
    }

    @ApiOperation(value = "查询我的客户列表(工具)", notes = "[郑朋]查询我的客户列表(工具)")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = QueryCustomerVO.class)
    })
    @RequestMapping(value = "customers", method = RequestMethod.GET)
    @AccessToken
    public Result getCustomerList(HttpServletRequest request, PageBean pageBean, QueryCustomerDTO queryCustomerDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        queryCustomerDTO.setManagerId(manageInfoVO.getId());
        log.info("(工具)根据条件查询我的客户入参：pageBean={},queryCustomerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(queryCustomerDTO));
        Result result = customerService.getToolCustomerByPage(pageBean, queryCustomerDTO);
        log.info("(工具)根据条件查询我的客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  toolId
     * @param @param  resuest
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addGridTool
     * @Description: 添加网格工具
     */
    @ApiOperation(value = "添加网格工具", notes = "[李杰]添加网格工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PostMapping("grid")
    public Result addGridTool(GridToolDTO dto, HttpServletRequest resuest) {
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = checkGridTool(dto);
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        ManageInfoVO info = getManageInfo(true);
        AddGridToolDTO requestDto = new AddGridToolDTO();
        List<GridToolDTO> datas = new ArrayList<GridToolDTO>();
        datas.add(dto);
        requestDto.setCreaterId(info.getId());
        requestDto.setDatas(datas);
        result = toolService.addGridTools(requestDto);
        log.info("添加网格工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: checkGridTool
     * @Description:校验数据
     */
    private String checkGridTool(BaseGridTool dto) {
        String result = dto.validateForm();
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        if (Double.valueOf(dto.getEntrustNum()) <= 0) {
            result = "委托数量不能小于0";
        } else if (Double.valueOf(dto.getBasePrice()) <= 0) {
            result = "基础价不能小于0";
        } else if (Double.valueOf(dto.getDifferencePrice()) <= 0) {
            result = "价差不能小于0";
        } else if (Double.valueOf(dto.getUpperPrice()) <= 0) {
            result = "上限价不能小于0";
        } else if (Double.valueOf(dto.getLowerPrice()) <= 0) {
            result = "下限价不能小于0";
        }
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  toolIdenty
     * @param @param  resuest
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addGridTool
     * @Description: 修改网格工具
     */
    @ApiOperation(value = "修改网格工具", notes = "[李杰]修改网格工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "toolIdenty", value = "工具唯一标识", paramType = "query", required = true)
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PutMapping("grid")
    public Result updateGridTool(BaseGridTool dto, String toolIdenty, HttpServletRequest resuest) {
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = checkGridTool(dto);
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        ManageInfoVO info = getManageInfo(true);
        Integer toolId = ToolUtils.getToolIdByTool(toolIdenty);
        UpdateGridToolDTO udto = new UpdateGridToolDTO();
        udto.setData(dto);
        udto.setToolId(toolId);
        udto.setUpdaterId(info.getId());
        result = toolService.updateGridTool(udto);
        log.info("修改网格工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  toolId
     * @param @param  resuest
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addSellingTool
     * @Description: 添加卖点工具
     */
    @ApiOperation(value = "添加卖点工具", notes = "[李杰]添加卖点工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)})
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PostMapping("selling")
    public Result addSellingTool(SellingToolDTO dto, HttpServletRequest resuest) {
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = checkSellingTool(dto);
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        ManageInfoVO info = getManageInfo(true);
        AddSellingToolDTO requestDto = new AddSellingToolDTO();
        List<SellingToolDTO> datas = new ArrayList<SellingToolDTO>();
        datas.add(dto);
        requestDto.setCreaterId(info.getId());
        requestDto.setDatas(datas);
        result = toolService.addSellingTools(requestDto);
        log.info("添加卖点工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: checkSellingTool
     * @Description: 参数校验
     */
    private String checkSellingTool(BaseSellingDTO dto) {
        String result = dto.validateForm();
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        if (Double.valueOf(dto.getEntrustNum()) <= 0) {
            result = "委托数量不能小于0";
        } else if (Integer.valueOf(dto.getShortDay()) <= 0) {
            result = "短期均线天数不能小于0";
        } else if (Integer.valueOf(dto.getLongDay()) <= 0) {
            result = "长期均线天数不能小于0";
        } else if (Integer.valueOf(dto.getShortDay()) >= Integer.valueOf(dto.getLongDay())) {
            result = "短期均线天数必须小于长期均线天数";
        } else if (Double.valueOf(dto.getTopDeviate()) <= 0) {
            result = "向上偏离值不能小于0";
        } else if (Double.valueOf(dto.getLowerDeviate()) <= 0) {
            result = "向下偏离值不能小于0";
        }
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  toolIdenty
     * @param @param  resuest
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addSellingTool
     * @Description:修改卖点工具
     */
    @ApiOperation(value = "修改卖点工具", notes = "[李杰]修改卖点工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "toolIdenty", value = "工具唯一标识", paramType = "query", required = true)
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PutMapping("selling")
    public Result updateSellingTool(BaseSellingDTO dto, String toolIdenty, HttpServletRequest resuest) {
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = checkSellingTool(dto);
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        ManageInfoVO info = getManageInfo(true);
        Integer toolId = ToolUtils.getToolIdByTool(toolIdenty);
        UpdateSellingToolDTO udto = new UpdateSellingToolDTO();
        udto.setData(dto);
        udto.setToolId(toolId);
        udto.setUpdaterId(info.getId());
        result = toolService.updateSellingTool(udto);
        log.info("修改卖点工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  userId
     * @param @param  toolId
     * @param @param  resuest
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addTrendTool
     * @Description: 添加趋势化工具
     */
    @ApiOperation(value = "添加趋势化工具", notes = "[李杰]添加趋势化工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "toolType", value = "工具类型：1、正常工具 2、专用工具", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "string", name = "dtos", value = "股票对象[{'stockCode':'','stockName':''}]", paramType = "qurey", required = true)
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PostMapping(value = "trend")
    public Result addTrendTool(String dtos, @RequestParam Byte toolType, HttpServletRequest resuest) {
        log.info("添加趋势化工具 controller 入参参数dtos={},toolType={}", dtos, toolType);
        ManageInfoVO info = getManageInfo(true);
        AddTrendToolDTO requestDto = new AddTrendToolDTO();
        requestDto.setCreaterId(info.getId());
        requestDto.setDatas(JSONArray.parseArray(dtos, TrendToolDTO.class));
        requestDto.setToolType(ToolTypeEnum.type(toolType));
        Result result = toolService.addTrendTool(requestDto);
        log.info("添加趋势化工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     *
     * @Title: addTrendTool
     * @Description: 修改趋势化工具
     * @param @param dto
     * @param @param toolIdenty
     * @param @param resuest
     * @param @return    参数
     * @return Result    返回类型
     * @throws
     */
	/*@ApiOperation(value = "修改趋势化工具",notes = "[李杰]修改趋势化工具")
    @ApiResponses(value = {
        @ApiResponse(code = 10100,message = "请求参数有误"),
        @ApiResponse(code = 200,message = "操作成功")
    })
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
		@ApiImplicitParam(dataType = "String", name = "toolIdenty", value = "工具唯一标识", paramType = "query", required = true)
    })
	@AccessToken
	@RepeatToken(isHeader = true, headerKey = "accessToken")
	@PutMapping("trend")
	public Result addTrendTool(TrendToolDTO dto, String toolIdenty, HttpServletRequest resuest) {
		ManageInfoVO info = getManageInfo(true);
		Integer toolId = ToolUtils.getToolIdByTool(toolIdenty);
		UpdateTrendToolDTO udto = new UpdateTrendToolDTO();
		udto.setData(dto);
		udto.setToolId(toolId);
		udto.setUpdaterId(info.getId());
		Result result = toolService.updateTrendTool(udto);
		log.info("修改趋势化工具返回数据result={}", JSON.toJSONString(result));
		return result;
	}*/

    /**
     * @param toolCode
     * @param userId
     * @param toolIds
     * @return
     * @Title: delete
     * @author: lijie
     * @Description: 删除工具
     * @return: Result
     */
    @ApiOperation(value = "删除工具（根据不同工具类别编码进行删除）", notes = "[李杰]删除工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "toolCode", value = "工具类别编码", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "String", name = "toolIdentys", value = "工具唯一标识:多个用逗号分隔", paramType = "query", required = true)
    })
    @AccessToken
    @DeleteMapping
    public Result delete(@RequestParam String toolCode, @RequestParam String toolIdentys) {
        ManageInfoVO info = getManageInfo(true);
        String[] strs = toolIdentys.split(",");
        List<Integer> toolIds = new ArrayList<>();
        List<Integer> toolBindIds = new ArrayList<>();
        Integer toolId;
        for (String str : strs) {
            toolId = ToolUtils.getToolIdByTool(str);
            if (null != toolId) {
                toolIds.add(toolId);
                continue;
            }
            toolId = ToolUtils.getToolIdByBind(str);
            if (null != toolId) {
                toolBindIds.add(toolId);
            }
        }
        Result result = toolService.deleteTool(toolCode, toolIds, toolBindIds, info.getId());
        log.info("删除工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param dto
     * @return
     * @Title: bind
     * @author: lijie
     * @Description: 绑定工具
     * @return: Result
     */
    @ApiOperation(value = "推荐工具", notes = "[李杰]推荐工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @PostMapping(value = "bind")
    public Result bind(BingToolDTO dto, HttpServletRequest resuest) {
        log.info("绑定工具入参数据dto={}", JSON.toJSONString(dto));
        dto.setCreaterId(getManageInfo(true).getId());
        Result result = toolService.bingTool(dto);
        log.info("绑定工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: toolBinds
     * @Description: 查询客户经理下面推荐给客户的工具列表
     */
    @ApiOperation(value = "查询客户经理下面推荐给客户的工具列表", notes = "[李杰]查询客户经理下面推荐给客户的工具列表")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ToolBindVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "customerId", value = "客户ID", paramType = "query", required = true)
    })
    @AccessToken
    @GetMapping(value = "/binds")
    public Result getBindTools(@RequestParam Integer customerId, PageBean pageBean) {
        Result result = toolService.getBindTools(getManageInfo(true).getId(), customerId, pageBean);
        log.info("查询客户经理下面推荐给客户的工具列表返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  bindId
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getBindInfo
     * @Description: 查询客户经理下面推荐给客户的单个工具详情
     */
    @ApiOperation(value = "查询客户经理下面推荐给客户的单个工具详情", notes = "[李杰]查询客户经理下面推荐给客户的单个工具详情")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ToolBindInfoVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "categoryCode", value = "工具类别编码", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "int", name = "bindId", value = "工具推荐ID", paramType = "query", required = true)
    })
    @AccessToken
    @GetMapping(value = "/binds/info")
    public Result getBindInfo(@RequestParam String categoryCode, @RequestParam Integer bindId, PageBean pageBean) {
        log.info("查询客户经理下面推荐给客户的单个工具详情入参={}", bindId);
        Result result = toolService.getBindToolInfo(bindId, categoryCode, pageBean);
        log.info("查询客户经理下面推荐给客户的单个工具详情返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: toolPush
     * @Description: 工具推送
     */
    @ApiOperation(value = "工具推送 ", notes = "[李杰]工具推送 ")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "toolIdenty", value = "工具标识ID", paramType = "form", required = true),
            @ApiImplicitParam(dataType = "String", name = "direction", value = "方向:买入、卖出", paramType = "form", required = true),
            @ApiImplicitParam(dataType = "String", name = "price", value = "价格", paramType = "form", required = true),
            @ApiImplicitParam(dataType = "int", name = "number", value = "成交数量(网格工具时传递)", paramType = "form"),
            @ApiImplicitParam(dataType = "String", name = "tradingTime", value = "交易时间：2015-10-20 00:00:00", paramType = "form", required = true),
            @ApiImplicitParam(dataType = "String", name = "stockCode", value = "股票代码", paramType = "form", required = true),
            @ApiImplicitParam(dataType = "String", name = "stockName", value = "股票代码名称", paramType = "form", required = true)
    })
    @PostMapping(value = "push")
    public Result toolPush(ToolPushDTO dto) {
        log.info("工具推送入参={}", JSON.toJSONString(dto));
        Result result = toolService.toolPush(dto);
        log.info("工具推送返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws
     * @Title: importToolShares
     * @Description: TODO
     * @param: @param file
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "导入趋势量化工具", notes = "[李杰]导入趋势量化工具 ")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "file", name = "file", value = "文件", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "int", name = "toolType", value = "工具类型：1、正常工具 2、专用工具", paramType = "query", required = true)
    })
    @ResponseBody
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importToolShares(HttpServletRequest resuest, @RequestParam Byte toolType) {
        log.info("导入工具入参toolType={}", toolType);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        List<TrendToolDTO> list = toolImportService.getTrendToolImportDatas(resuest);
        if (null != list && !list.isEmpty()) {
            ManageInfoVO info = getManageInfo(true);
            AddTrendToolDTO requestDto = new AddTrendToolDTO();
            requestDto.setCreaterId(info.getId());
            requestDto.setDatas(list);
            requestDto.setToolType(ToolTypeEnum.type(toolType));
            result = toolService.addTrendTool(requestDto);
            log.info("导入趋势化工具返回数据result={}", JSON.toJSONString(result));
        }
        return result;
    }

    /**
     * @param @param  toolIdentys
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getTrendRealTimeDatas
     * @Description:工具刷新
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "工具刷新 ", notes = "[李杰]工具刷新 ")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ToolCacheVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "toolIdentys", value = "工具标识ID多个则用逗号给开", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "int", name = "toolType", value = "工具类型：1、常规 2、专用", paramType = "path", required = true)
    })
    @AccessToken
    @GetMapping("refresh/{toolIdentys}/{toolType}")
    public Result getTrendRealTimeDatas(@PathVariable String toolIdentys, @PathVariable Byte toolType) {
        log.info("刷新工具实时数据入参={},toolType={}", toolIdentys, toolType);
        List<ToolCacheVO> rlist = new ArrayList<>();
        String[] strs = toolIdentys.split(",");
        List<String> bindTools = new ArrayList<>();
        List<String> tools = new ArrayList<>();
        for (String str : strs) {
            if (str.startsWith(ToolUtils.TOOLBIND)) {
                bindTools.add(str);
            } else if (str.startsWith(ToolUtils.TOOL)) {
                tools.add(str);
            }
        }
        Map<String, ToolCacheVO> map = new HashMap<>();
        Result result = toolCacheService.getToolBindRealTimeDatas(ToolCategoryEnum.TREND, bindTools, toolType);
        log.info("查询绑定的实时数据返回结果={}", JSON.toJSONString(result));
        if (result.isSuccess()) {
            map.putAll((Map<String, ToolCacheVO>) result.getData());
        }
        result = toolCacheService.getToolRealTimeDatas(ToolCategoryEnum.TREND, tools);
        log.info("查询未绑定的实时数据返回结果={}", JSON.toJSONString(result));
        if (result.isSuccess()) {
            map.putAll((Map<String, ToolCacheVO>) result.getData());
        }
        ToolCacheVO vo;
        for (int i = 0; i < strs.length; i++) {
            vo = map.get(strs[i]);
            if (null == vo) {
                vo = initToolCacheVO();
            }
            vo.setToolIdenty(strs[i]);
            rlist.add(vo);
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS, rlist);
    }

    @ApiOperation(value = "专用趋势化工具详情 ", notes = "[李杰]专用趋势化工具详情 ")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ToolCacheVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
    })
    @AccessToken
    @GetMapping("/special/info")
    public Result getSpecialToolDetail(ToolSpecialDTO toolSpecialDTO, PageBean pageBean) {
        log.info("专用趋势化工具详情 toolSpecialDTO={},pageBean={}", JSON.toJSONString(toolSpecialDTO), JSON.toJSONString(pageBean));
        Result result = toolService.getSpecialToolInfo(toolSpecialDTO, pageBean);
        log.info("专用趋势化工具详情 result={},", JSON.toJSONString(result));
        return result;
    }

    private ToolCacheVO initToolCacheVO() {
        ToolCacheVO vo = new ToolCacheVO();
        vo.setDirection("-");
        vo.setLatestPrice("-");
        return vo;
    }
}
