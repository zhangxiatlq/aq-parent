package com.aq.controller.pc.flower;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.vo.account.ManagerFlowerVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户经理 消费流水 controller
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
@RestController
@RequestMapping("/api/pc/manager/flowers")
@Api(value = "客户经理账户流水接口", description = "客户经理账户流水接口")
@Slf4j
public class ManagerFlowerController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IAccountFlowerService accountFlowerService;

    @ApiOperation(value = "账户流水列表", notes = "[郑朋]账户流水列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = ManagerFlowerVO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @AccessToken
    public Result getManagerFlower(HttpServletRequest request, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        AccountFlowerDTO accountFlowerDTO = new AccountFlowerDTO();
        accountFlowerDTO.setAccountId(manageInfoVO.getId());
        accountFlowerDTO.setRoleType(RoleCodeEnum.MANAGER.getCode());
        accountFlowerDTO.setManagerId(manageInfoVO.getId());
        log.info("账户流水列表入参：pageBean={},accountFlowerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(accountFlowerDTO));
        Result result = accountFlowerService.getAccountFlower(pageBean, accountFlowerDTO);
        log.info("账户流水列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }


}
