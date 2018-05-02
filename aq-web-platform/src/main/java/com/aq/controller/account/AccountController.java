package com.aq.controller.account;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.service.account.IBalanceService;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：我的账户和账户流水 controller
 * @author： 张霞
 * @createTime： 2018/03/26
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Controller
@RequestMapping(value = "web/account")
public class AccountController extends BaseController{

    @Reference(version = "1.0.0")
    IBalanceService balanceService;

    @Reference(version = "1.0.0")
    IAccountFlowerService accountFlowerService;
    /**
     * @author: zhangxia
     * @desc:页面跳转至我的账户 页面
     * @params: []
     * @methodName:page
     * @date: 2018/3/26 0026 上午 11:22
     * @return: java.lang.String
     * @version:2.1.2
     */
    @RequestMapping(value = "page",method = RequestMethod.GET)
    public String page(Model model){
        SessionUserVO sessionUserVO = getLoginUser();
        Result result = balanceService.getBalance(sessionUserVO.getId(), RoleCodeEnum.ADMIN.getCode());
        model.addAttribute("userName", sessionUserVO.getUserName());
//        model.addAttribute("accountId", accountId);
        model.addAttribute("vo", result.getData());
        return "account/mineAccountFlower";
    }

    /**
     * @author: zhangxia
     * @desc: 我的账户  菜单页面中的账户流水列表 分页查询
     * @params: [pageBean, dto]
     * @methodName:accountFlowerList
     * @date: 2018/3/26 0026 下午 13:59
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Result accountFlowerList(PageBean pageBean,AccountFlowerDTO dto){
        log.info("我的账户-菜单页面中的账户流水列表 分页查询 入参参数pageBean={},dto={}",JSON.toJSONString(pageBean),JSON.toJSONString(dto));
        SessionUserVO sessionUserVO = getLoginUser();

        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.NOT_LOGIN);
        }
        dto.setRoleType(RoleCodeEnum.ADMIN.getCode());
        dto.setUserId(sessionUserVO.getId());
        if (StringUtils.isNotEmpty(dto.getCreateTimeStart())) {
            dto.setCreateTimeStart(dto.getCreateTimeStart() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(dto.getCreateTimeEnd())) {
            dto.setCreateTimeEnd(dto.getCreateTimeEnd() + " 23:59:59");
        }
        Result result =  accountFlowerService.getAccountFlowerList(pageBean, dto);
        log.info("我的账户-账户流水 列表返回值， result={}", JSON.toJSONString(result));;
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 账户流水-跳转页面
     * @params: []
     * @methodName:page
     * @date: 2018/3/26 0026 下午 14:35
     * @return: java.lang.String
     * @version:2.1.2
     */
    @RequestMapping(value = "list/page" , method = RequestMethod.GET)
    public String page(){
        return "account/allAccountFlower";
    }

    /**
     * @author: zhangxia
     * @desc: 账户流水-列表查询
     * @params: [pageBean, dto]
     * @methodName:allAccountFlowerList
     * @date: 2018/3/26 0026 下午 14:38
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @RequestMapping(value = "list/list")
    @ResponseBody
    public Result allAccountFlowerList(PageBean pageBean,AccountFlowerDTO dto){
        log.info("账户流水-菜单页面中的账户流水列表 分页查询 入参参数pageBean={},dto={}",JSON.toJSONString(pageBean),JSON.toJSONString(dto));
        SessionUserVO sessionUserVO = getLoginUser();

        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.NOT_LOGIN);
        }
        if (StringUtils.isNotEmpty(dto.getCreateTimeStart())) {
            dto.setCreateTimeStart(dto.getCreateTimeStart() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(dto.getCreateTimeEnd())) {
            dto.setCreateTimeEnd(dto.getCreateTimeEnd() + " 23:59:59");
        }
        Result result =  accountFlowerService.getAllAccountFlowerList(pageBean, dto);
        log.info("账户流水-账户流水 列表返回值， result={}", JSON.toJSONString(result));;
        return result;
    }
}
