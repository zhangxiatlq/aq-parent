package com.aq.controller.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.SmsTypeEnum;
import com.aq.core.sms.AliySmsSendCore;
import com.aq.facade.service.customer.ICustomerManageService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: SmsController
 * @Description: 短信发送
 * @author: lijie
 * @date: 2018年2月9日 下午3:55:21
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/sms")
@Api(value = "短信发送", description = "短信发送")
public class SmsController {

	@Reference(version = "1.0.0", check = false)
	private ICustomerService customerService;
	
	@Reference(version = "1.0.0",check=false)
	private ICustomerManageService customerManageService;
	
	@Autowired
	private AliySmsSendCore smsSendCore;
	/**
	 * 
	 * @Title: sendSms
	 * @author: lijie 
	 * @Description: 发送短信验证码
	 * @return
	 * @return: Result
	 */
	@ApiOperation(value = "发送短信验证码", notes = "[李杰]发送短信验证码")
    @ApiResponses(value = {
			@ApiResponse(code = 200, message = "操作成功")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "telphone", value = "手机号", paramType = "path" , required = true),
		@ApiImplicitParam(dataType = "Byte", name = "role", value = "角色：2、客户,3、经理", paramType = "path" , required = true),
		@ApiImplicitParam(dataType = "String", name = "type", value = "类型：AQ1000001(注册),AQ1000002(找回密码),AQ1000003(设置支付密码)", paramType = "path" , required = true)
	})
	@RequestMapping(value = "send/{telphone}/{type}/{role}", method = RequestMethod.GET)
	public Result sendSms(@PathVariable String telphone, @PathVariable String type, @PathVariable Byte role) {
		log.info("发送短信验证码入参={},type={},role={}", telphone, type, role);
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		RoleCodeEnum roleType = RoleCodeEnum.getRoleEnumByCode(role);
		if (null == roleType) {
			log.warn("短信发送角色不存在");
			return result;
		}
		SmsTypeEnum typeEnum = SmsTypeEnum.type(type);
		// 注册时校验 如果存在则不发送
		if (typeEnum == SmsTypeEnum.REGISTER) {
			if (check(roleType, telphone)) {
				result.setMessage("账号已被注册");
				return result;
			}
		}
		if (typeEnum.isCheck()) {
			if (!check(roleType, telphone)) {
				result.setMessage("账号不存在");
				return result;
			}
		}
		result = smsSendCore.sendSmsCode(telphone, typeEnum, roleType);
		log.info("短信发送返回数据={}", JSON.toJSONString(result));
		return result;
	}
	/**
	 * 
	* @Title: check  
	* @Description: 校验用户 
	* @param @param roleType
	* @param @param telphone
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	private boolean check(final RoleCodeEnum roleType, final String telphone) {
		boolean flag = false;
		switch (roleType) {
		case CUSTOMER:
			flag = customerService.getCustomerByAccount(telphone).isSuccess();
			break;
		case MANAGER:
			flag = customerManageService.getManageByAccount(telphone).isSuccess();
			break;
		default:
			break;
		}
		return flag;
	}
}
