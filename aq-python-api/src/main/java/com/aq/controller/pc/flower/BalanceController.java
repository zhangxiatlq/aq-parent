package com.aq.controller.pc.flower;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.facade.dto.account.DrawBalanceDTO;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.vo.account.DrawCashVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.http.IPUtil;
import com.aq.util.result.Result;
import com.aq.util.string.StringTools;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 余额controller
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@RestController
@RequestMapping("/api/pc/balance")
@Api(value = "客户经理余额接口", description = "客户经理余额接口")
@Slf4j
public class BalanceController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IAccountFlowerService accountFlowerService;

    @ApiOperation(value = "提现余额信息", notes = "[郑朋]提现余额信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = DrawCashVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getBalanceInfo() {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        log.info("提现余额信息入参：managerId={}", manageInfoVO.getId());
        Result result = accountFlowerService.getBalanceInfo(manageInfoVO.getId());
        if (result.isSuccess()) {
            DrawCashVO drawCashVO = result.getData(DrawCashVO.class);
            drawCashVO.setPayPassword(null);
            drawCashVO.setBankcard(StringTools.bankNoChange(drawCashVO.getBankcard()));
            drawCashVO.setRealName(StringTools.realNameChange(drawCashVO.getRealName()));
        }
        log.info("提现余额信息返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "提现申请", notes = "[郑朋]提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = DrawCashVO.class)
    })
    @RequestMapping(value = "/draw", method = RequestMethod.GET)
    @RepeatToken(isHeader = true, headerKey = "accessToken")
    @AccessToken
    public Result drawCash(HttpServletRequest request, DrawBalanceDTO drawBalanceDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        drawBalanceDTO.setManagerId(manageInfoVO.getId());
        drawBalanceDTO.setRequestIp(IPUtil.getIpAddress(request));
        log.info("提现提现申请入参：drawBalanceDTO={}", JSON.toJSONString(drawBalanceDTO));
        Result result = accountFlowerService.drawBalance(drawBalanceDTO);
        log.info("提现提现申请返回值：result={}", JSON.toJSONString(result));
        return result;
    }

}
