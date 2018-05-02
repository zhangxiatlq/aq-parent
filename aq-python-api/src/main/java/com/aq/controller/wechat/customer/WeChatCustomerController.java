package com.aq.controller.wechat.customer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.controller.wechat.customer.dto.CustomerRegisterDTO;
import com.aq.controller.wechat.customer.dto.LoginDTO;
import com.aq.controller.wechat.customer.vo.ApiCustomerInfoVO;
import com.aq.core.annotation.RepeatToken;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.SmsTypeEnum;
import com.aq.core.sms.AliySmsSendCore;
import com.aq.core.wechat.constant.TagEnum;
import com.aq.facade.dto.ApplyDTO;
import com.aq.facade.dto.customer.AddCustomerDTO;
import com.aq.facade.dto.customer.CustomerLoginDTO;
import com.aq.facade.dto.customer.UpdateCustomerDTO;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.facade.dto.share.dto.UpdateLoginPasswordDTO;
import com.aq.facade.enums.PwdOperTypeEnum;
import com.aq.facade.exception.customer.CustomerExceptionEnum;
import com.aq.facade.service.IToolService;
import com.aq.facade.service.wechat.IWechatUserService;
import com.aq.facade.vo.ToolNumberVO;
import com.aq.facade.vo.ToolPushVO;
import com.aq.facade.vo.WeChatToolVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.util.http.RequestUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;

/**
 * @ClassName: CustomerController
 * @Description: 客户
 * @author: lijie
 * @date: 2018年2月11日 下午5:43:21
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/wechat/customers")
@Api(value = "微信客户相关接口", description = "微信客户相关接口")
public class WeChatCustomerController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IToolService toolService;

    @Autowired
    private AliySmsSendCore smsSendCore;

    @Reference(version = "1.0.0", check = false)
    private IWechatUserService wechatUserService;

    @Autowired
	private Executor asyncTaskPool;
    /**
     * @param dto
     * @param request
     * @return
     * @Title: register
     * @author: lijie
     * @Description: 客户注册
     * @return: Result
     */
    @ApiOperation(value = "注册", notes = "[李杰]注册")
    @ApiImplicitParams({
        @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功")
    })
	@RepeatToken(key = "telphone")
	@PostMapping("customer/register")
	public Result register(CustomerRegisterDTO dto, HttpServletRequest request) {
		log.info("客户注册入参={}", JSON.toJSONString(dto));
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		String openId = RequestUtil.getOpenId(request);
		if (StringUtils.isBlank(openId)) {
			result.setMessage("微信标识不能为空[openId]");
			return result;
		}
		result = smsSendCore.checkSmsCode(dto.getTelphone(), dto.getSmsCode(), SmsTypeEnum.REGISTER,
				RoleCodeEnum.CUSTOMER);
		if (!result.isSuccess()) {
			return result;
		}
		AddCustomerDTO adddto = new AddCustomerDTO();
		adddto.setCreaterId(0);
		adddto.setPassword(dto.getPassword());
		adddto.setRealName(dto.getRealName());
		adddto.setTelphone(dto.getTelphone());
		adddto.setUsername(dto.getUsername());
		adddto.setIdCode(dto.getIdCode());
		adddto.setOpenId(openId);
		result = customerService.registerCustomer(adddto);
		log.info("客户注册返回数据={}", JSON.toJSONString(result));
		if (result.isSuccess()) {
			result = customerService.updateCustomerOpenId(dto.getTelphone(), openId);
			if (result.isSuccess()) {
				// 为客户打上标签
				setTag(openId);
			}
		}
		return result;
	}
    /**
     *
    * @Title: setTag
    * @Description: 为客户打上标签
    * @param: @param openId
    * @return void
    * @author lijie
    * @throws
     */
	private void setTag(String openId) {
		try {
			asyncTaskPool.execute(new Runnable() {

				public void run() {

					Result tagResult = wechatUserService.addUserTag(TagEnum.FOLLOW_CUSTOMER_TAG.getTagId(), openId);
					log.info("注册/登录成功为客户打上标签返回结果={}", JSON.toJSONString(tagResult));
				}

			});
		} catch (Exception e) {
			log.error("注册/登录成功为客户打上标签失败", e);
		}
	}
    /**
     * @param dto
     * @param request
     * @return
     * @Title: register
     * @author: lijie
     * @Description: 登录接口
     * @return: Result
     */
    @ApiOperation(value = "登录", notes = "[李杰]登录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功")
    })
	@RepeatToken(key = "account")
	@PostMapping("customer/login")
	public Result register(LoginDTO dto, HttpServletRequest request) {
		log.info("登录入参={}", JSON.toJSONString(dto));
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		String openId = RequestUtil.getOpenId(request);
		if (StringUtils.isBlank(openId)) {
			result.setMessage("微信标识不能为空[openId]");
			return result;
		}
		CustomerLoginDTO login = new CustomerLoginDTO();
		login.setAccount(dto.getAccount());
		login.setPassword(dto.getPassword());
		result = customerService.customerLogin(login);
		log.info("客户登录返回数据={}", JSON.toJSONString(result));
		if (result.isSuccess()) {
			result = customerService.updateCustomerOpenId(result.getData().toString(), openId);
			if (result.isSuccess()) {
				// 为客户打上标签
				setTag(openId);
			}
		}
		return result;
	}

    /**
     * @param @param  request
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: backLoginPwd
     * @Description: 忘记密码
     */
    @ApiOperation(value = "忘记密码", notes = "[李杰]忘记密码")
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功")
    })
    @PostMapping("customer/password")
    public Result backLoginPwd(UpdateLoginPasswordDTO dto, HttpServletRequest request) {
        log.info("客户找回登录密码入参={}", JSON.toJSONString(dto));
        Result result = smsSendCore.checkSmsCode(dto.getTelphone(), dto.getSmsCode(), SmsTypeEnum.BACK_PASSWORD,
                RoleCodeEnum.CUSTOMER);
        if (!result.isSuccess()) {
            return result;
        }
        LoginPasswordDTO udto = new LoginPasswordDTO();
        udto.setNewPassword(dto.getNewPwd());
        udto.setTelphone(dto.getTelphone());
        udto.setType(PwdOperTypeEnum.BACK);
        result = customerService.updateCustomerLoginPwd(udto);
        log.info("客户找回密码返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  request
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: backLoginPwd
     * @Description: 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "[李杰]修改密码")
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功")
    })
    @PutMapping("customer/password")
    public Result updateLoginPwd(UpdateLoginPasswordDTO dto) {
        log.info("客户修改登录密码入参={}", JSON.toJSONString(dto));
        LoginPasswordDTO udto = new LoginPasswordDTO();
        udto.setPassword(dto.getUsedPwd());
        udto.setNewPassword(dto.getNewPwd());
        udto.setTelphone(dto.getTelphone());
        udto.setType(PwdOperTypeEnum.UPDATE);
        Result result = customerService.updateCustomerLoginPwd(udto);
        log.info("客户修改登录密码返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param
     * @return
     * @Title: checkBind
     * @author: lijie
     * @Description: 查询个人信息
     * @return: Result
     */
    @ApiOperation(value = "查询个人信息", notes = "[李杰]查询个人信息")
    @ApiImplicitParams({
        @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功",response = ApiCustomerInfoVO.class)
    })
	@GetMapping("customer/info/{openId}")
	public Result checkBind(HttpServletRequest request) {
		CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
		if (null == vo) {
			return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
		}
		Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
		ApiCustomerInfoVO rinfo = new ApiCustomerInfoVO();
		BeanUtils.copyProperties(vo, rinfo);
		// TODO:临时将客户真实姓名改成手机号
		rinfo.setRealName(vo.getTelphone());
		result.setData(rinfo);
		return result;
	}

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: updateUserName
     * @Description: 客户修改用户名
     */
    @ApiOperation(value = "修改用户名", notes = "[李杰]修改用户名")
    @ApiImplicitParams({
        @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
        @ApiImplicitParam(dataType = "String", name = "userName", value = "用户姓名", paramType = "form", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功")
    })
    @PutMapping("customer")
    public Result updateUserName(@RequestParam String userName, HttpServletRequest request) {
        log.info("修改用户名入参={}", userName);
        CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
        if (null == vo) {
            return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
        }
        UpdateCustomerDTO udto = new UpdateCustomerDTO();
        udto.setId(vo.getId());
        udto.setUsername(userName);
        Result result = customerService.updateCustomer(udto);
        log.info("修改客户返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param dto
     * @param request
     * @return
     * @Title: applyTool
     * @author: lijie
     * @Description: 申请工具
     * @return: Result
     */
    @ApiOperation(value = "申请工具", notes = "[李杰]申请工具")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
         @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @RepeatToken(isHeader = true, headerKey = "OPENID")
    @PostMapping("applytool")
    public Result applyTool(ApplyDTO dto, HttpServletRequest request) {
        log.info("申请工具如参={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        CustomerInfoVO userInfo = getCustomerInfo(RequestUtil.getOpenId(request));
        if (null == userInfo) {
            return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
        }
        if (null == userInfo.getManagerId()) {
            result.setMessage("当前用的客户经理不存在");
            return result;
        }
        dto.setCustomerId(userInfo.getId());
        dto.setManagerId(userInfo.getManagerId());
        dto.setOpenId(userInfo.getOpenId());
        result = toolService.applyTool(dto);
        log.info("申请工具返回数据result={}", JSON.toJSONString(result));
        return result;
    }
    /**
     * 
    * @Title: getApplyToolNum  
    * @Description: 获取工具剩余申请数量 
    * @param @param toolCategoryCode
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
    @ApiOperation(value = "获取工具剩余申请数量", notes = "[李杰]获取工具剩余申请数量")
    @ApiResponses(value = {
         @ApiResponse(code = 10100, message = "请求参数有误"),
         @ApiResponse(code = 200, message = "操作成功",response = ToolNumberVO.class)
    })
    @ApiImplicitParams({
         @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
         @ApiImplicitParam(dataType = "String", name = "toolCategoryCode", value = "工具类别编码", paramType = "path", required = true)
    })
	@GetMapping("tool/number/{toolCategoryCode}")
	public Result getApplyToolNum(@PathVariable String toolCategoryCode, HttpServletRequest request) {
		log.info("获取工具剩余申请数量入参={}", toolCategoryCode);
		CustomerInfoVO userInfo = getCustomerInfo(RequestUtil.getOpenId(request));
		if (null == userInfo) {
			return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
		}
		Result result = toolService.getApplyToolSurplusNum(userInfo.getId(), toolCategoryCode);
		log.info("获取工具剩余申请数量返回数据={}", JSON.toJSONString(result));
		return result;
	}
    /**
     * @param openId
     * @param pageBean
     * @return
     * @Title: datas
     * @author: lijie
     * @Description: 查询我的工具列表
     * @return: Result
     */
    @ApiOperation(value = "查询我的工具列表", notes = "[李杰]查询我的工具列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "获取数据成功", response = WeChatToolVO.class)
    })
    @GetMapping("tool/datas")
    public Result datas(HttpServletRequest request, PageBean pageBean) {
        CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
        if (null == vo) {
            return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
        }
        Result result = toolService.getToolsByPage(vo.getId(), pageBean);
        log.info("查询我的工具列表返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  request
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: loadPushs
     * @Description: 查询单个工具推送列表
     */
    @ApiOperation(value = "查询单个工具推送列表", notes = "[李杰]查询单个工具推送列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "bindId", value = "工具推荐ID", paramType = "query", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "获取数据成功", response = ToolPushVO.class)
    })
    @GetMapping("tool/push/datas")
    public Result loadPushs(HttpServletRequest request, @RequestParam Integer bindId, PageBean pageBean) {
        log.info("查询单个工具推送列表入参={},pageBean={}", bindId, JSON.toJSONString(pageBean));
        CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
        if (null == vo) {
            return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
        }
        Result result = toolService.getPushsByPage(bindId, pageBean);
        log.info("查询单个工具推送列表返回数据result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 判断openId 是否注册
     *
     * @param openId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/24 0024
     */
    @ApiOperation(value = "判断openId 是否注册", notes = "[郑朋]判断openId 是否注册")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "path", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "获取数据成功")
    })
    @GetMapping("customer/check/{openId}")
    public Result checkOpenid(@PathVariable String openId) {
        log.info("判断openId 是否注册入参={},openId={}", openId);
        Result result = customerService.checkOpenId(openId);
        log.info("判断openId 是否注册返回数据result={}", JSON.toJSONString(result));
        return result;
    }
    /**
     * 
    * @Title: getStockInfo  
    * @Description: TODO(这里用一句话描述这个方法的作用)  
    * @param @param stockCode
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
    @ApiOperation(value = "根据股票代码获取股票信息", notes = "[李杰]根据股票代码获取股票信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "stockCode", value = "股票代码", paramType = "path", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "获取数据成功")
    })
	@GetMapping("stock/{stockCode}")
	public Result getStockInfo(@PathVariable String stockCode) {
		log.info("根据股票代码获取股票信息入参={}", stockCode);
		return toolService.getStockInfo(stockCode);
	}
}
