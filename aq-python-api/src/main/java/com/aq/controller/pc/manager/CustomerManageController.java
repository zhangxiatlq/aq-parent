package com.aq.controller.pc.manager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.controller.pc.manager.dto.UpdatePayPwdDTO;
import com.aq.core.annotation.RepeatToken;
import com.aq.core.config.properties.WebProperties;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.SmsTypeEnum;
import com.aq.core.sms.AliySmsSendCore;
import com.aq.facade.dto.manage.*;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.facade.dto.share.dto.UpdateLoginPasswordDTO;
import com.aq.facade.enums.PwdOperTypeEnum;
import com.aq.facade.service.customer.ICustomerManageService;
import com.aq.facade.service.manager.IManagerInfoService;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.facade.vo.manage.ManagerBaseInfoVO;
import com.aq.util.http.IPUtil;
import com.aq.util.qrcode.QRCodeUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: CustomerManageController
 * @Description: 客户经理接口
 * @author: lijie
 * @date: 2018年2月9日 下午4:50:06
 */
@RestController
@RequestMapping("/api/pc/manages")
@Api(value = "客户经理接口", description = "客户经理接口")
@Slf4j
public class CustomerManageController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private ICustomerManageService customerManageService;

    @Reference(version = "1.0.0", check = false)
    private IManagerInfoService managerInfoService;

    @Autowired
    private AliySmsSendCore smsSendCore;

    @Autowired
    private WebProperties webConfig;

    /**
     * @param @param  dto
     * @param @param  request
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: login
     * @Description: 客户经理登录
     */
    @ApiOperation(value = "客户经理登录", notes = "[李杰]客户经理登录")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManageInfoVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(LoginDTO dto, HttpServletRequest request) {
        log.info("客户经理登录入参={}", JSON.toJSONString(dto));
        Result result = customerManageService.login(dto);
        log.info("客户经理登录返回数据={}", JSON.toJSONString(result));
        // String str = decryptByPrivateKey(dto.getPassword());
        if (result.isSuccess()) {
            ManageInfoVO vo = ((ManageInfoVO) result.getData());
            super.save(vo);
            result.setData(vo);
        }
        return result;
    }

    /**
     * @param @param  dto
     * @param @param  request
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: register
     * @Description: 注册
     */
    @ApiOperation(value = "客户经理注册", notes = "[李杰]客户经理注册")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManageInfoVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @RepeatToken(key = "telphone")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(RegisterDTO dto, HttpServletRequest request) {
        log.info("客户经理注册入参={}", JSON.toJSONString(dto));
        Result result = smsSendCore.checkSmsCode(dto.getTelphone(), dto.getSmsCode(), SmsTypeEnum.REGISTER,
                RoleCodeEnum.MANAGER);
        if (!result.isSuccess()) {
            return result;
        }
        result = customerManageService.registerManage(dto);
        log.info("客户经理注册返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: updateLoginPwd
     * @Description: 客户经理找回登录密码
     */
    @ApiOperation(value = "客户经理找回登录密码", notes = "[李杰]客户经理找回登录密码")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @RequestMapping(value = "/login/password", method = RequestMethod.POST)
    public Result backLoginPwd(UpdateLoginPasswordDTO dto) {
        log.info("客户经理找回登录密码入参={}", JSON.toJSONString(dto));
        Result result = smsSendCore.checkSmsCode(dto.getTelphone(), dto.getSmsCode(), SmsTypeEnum.BACK_PASSWORD,
                RoleCodeEnum.MANAGER);
        if (!result.isSuccess()) {
            return result;
        }
        LoginPasswordDTO udto = new LoginPasswordDTO();
        udto.setNewPassword(dto.getNewPwd());
        udto.setTelphone(dto.getTelphone());
        udto.setType(PwdOperTypeEnum.BACK);
        result = customerManageService.updateLoginPwd(udto);
        log.info("客户经理找回密码返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: updateLoginPwd
     * @Description: 客户经理修改登录密码
     */
    @ApiOperation(value = "客户经理修改登录密码", notes = "[李杰]客户经理修改登录密码")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @AccessToken
    @RequestMapping(value = "/login/password", method = RequestMethod.PUT)
    public Result updateLoginPwd(UpdateLoginPasswordDTO dto) {
        log.info("客户经理修改登录密码入参={}", JSON.toJSONString(dto));
        LoginPasswordDTO udto = new LoginPasswordDTO();
        udto.setPassword(dto.getUsedPwd());
        udto.setNewPassword(dto.getNewPwd());
        udto.setTelphone(dto.getTelphone());
        udto.setType(PwdOperTypeEnum.UPDATE);
        Result result = customerManageService.updateLoginPwd(udto);
        log.info("客户经理修改登录密码返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: payPwd
     * @Description: 客户经理设置/修改支付密码
     */
    @ApiOperation(value = "客户经理设置/修改支付密码", notes = "[李杰]客户经理设置/修改支付密码")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @AccessToken
    @RequestMapping(value = "/pay/password", method = RequestMethod.POST)
    public Result payPwd(UpdatePayPwdDTO dto) {
        log.info("客户经理设置/修改支付密码入参={}", JSON.toJSONString(dto));
        Result result = smsSendCore.checkSmsCode(dto.getTelphone(), dto.getSmsCode(), SmsTypeEnum.BACK_PAY_PASSWORD,
                RoleCodeEnum.MANAGER);
        if (!result.isSuccess()) {
            return result;
        }
        ManageInfoVO info = getManageInfo(true);
        PayPwdDTO udto = new PayPwdDTO();
        udto.setOperId(info.getId());
        udto.setPayPwd(dto.getPayPwd());
        udto.setTelphone(dto.getTelphone());
        result = customerManageService.updatePayPwd(udto);
        log.info("客户经理设置/修改支付密码返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 客户经理绑定银行卡
     *
     * @param request
     * @param bindBankDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    @ApiOperation(value = "客户经理绑定银行卡", notes = "[郑朋]客户经理绑定银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @RequestMapping(value = "manager/bank/binding", method = RequestMethod.POST)
    public Result bindBank(HttpServletRequest request, BindBankDTO bindBankDTO) {
        ManageInfoVO info = getManageInfo(true);
        bindBankDTO.setCreateIp(IPUtil.getIpAddress(request));
        bindBankDTO.setManagerId(info.getId());
        log.info("客户经理绑定银行卡入参, bindBankDTO={}", JSON.toJSONString(bindBankDTO));
        Result result = managerInfoService.bindBankForManager(bindBankDTO);
        log.info("客户经理绑定银行卡返回数据, result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 客户经理绑定银行卡
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    @ApiOperation(value = "客户经理基本信息", notes = "[郑朋]客户经理基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManagerBaseInfoVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @AccessToken
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public Result info() {
        ManageInfoVO info = getManageInfo(true);
        log.info("客户经理绑定银行卡入参, id={}", info.getId());
        Result result = managerInfoService.getManagerInfo(info.getId());
        log.info("客户经理绑定银行卡返回数据, result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 客户经理绑定银行卡
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    @ApiOperation(value = "修改头像", notes = "[郑朋]修改头像")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManagerBaseInfoVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @AccessToken
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @RequestMapping(value = "/manager", method = RequestMethod.PUT)
    public Result modifyManager(UpdateManagerDTO updateManagerDTO) {
        ManageInfoVO info = getManageInfo(true);
        updateManagerDTO.setManagerId(info.getId());
        log.info("修改客户经理头像入参, updateManagerDTO={}", JSON.toJSONString(updateManagerDTO));
        Result result = managerInfoService.modifyManager(updateManagerDTO);
        log.info("修改客户经理头像返回数据, result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * 微信绑定二维码生成
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    @ApiOperation(value = "微信绑定二维码生成", notes = "[郑朋]微信绑定二维码生成")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "telphone", value = "手机号", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "int", name = "roleType", value = "角色类型(2-客户,3-客户经理)", paramType = "path", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManagerBaseInfoVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @RequestMapping(value = "/qrcode/{telphone}/{roleType}", method = RequestMethod.GET)
    public void QRCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String telphone, @PathVariable String roleType) {
        StringBuilder stringBuilder = new StringBuilder(webConfig.getPythonWebChat())
                .append("?account=")
                .append(telphone)
                .append("&roleCode=").append(roleType);
        QRCodeUtil.zxingCodeCreate(request, response, stringBuilder.toString(), 300, 300);
    }

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: refreshToken
     * @Description: 刷新token
     */
    @ApiOperation(value = "刷新客户经理登录缓存token", notes = "[李杰]刷新客户经理登录缓存token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
	@AccessToken
	public Result refreshToken(HttpServletRequest request) {
		return ResultUtil.getResult(RespCode.Code.SUCCESS);
	}
}
