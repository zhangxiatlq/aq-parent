package com.aq.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.UserStatusEnum;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.facade.entity.permission.User;
import com.aq.facade.service.IClientService;
import com.aq.facade.service.customer.ICustomerManageService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户controller
 * @作者： 张霞
 * @创建时间： 11:09 2018/1/22
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Controller
@RequestMapping(value = "/web/client/")
public class ClientController extends BaseController{

    @Reference(version = "1.0.0")
    IClientService clientService;

    @Reference(version = "1.0.0")
    IUserService userService;

    @Reference(version = "1.0.0")
    ICustomerManageService managerService;

    @Reference(version = "1.0.0")
    ICustomerService customerService;

    /**
     * @author: 张霞
     * @description：跳转到客户列表页面
     * @createDate: 11:13 2018/1/22
     * @param
     * @return: java.lang.String
     * @version: 2.1
     */
    @RequestMapping(value = "page",method = RequestMethod.GET)
    public String toListPage(){

        return "user/client";
    }
    /**
     * @Creater: zhangxia
     * @description：翻页获取客户列表
     * @methodName: getCustomerListByPage
     * @params: [pageBean, selectClientDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 9:02 2018-3-1
     */
    @RequestMapping(value = "list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getCustomerListByPage(PageBean pageBean, SelectClientDTO selectClientDTO){

        log.info("翻页获取客户列表controller层入参参数pageBean={},selectClientDTO={}",JSON.toJSONString(pageBean), JSON.toJSONString(selectClientDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        selectClientDTO.setUserId(sessionUserVO.getId());
        selectClientDTO.setRoleCode(sessionUserVO.getSessionRoleVo().get(0).getRoleCode());
        return customerService.getCustomerListByPage(pageBean,selectClientDTO);
    }

    /**
     * @author: 张霞
     * @description：获取客户详情信息
     * @createDate: 11:25 2018/1/22
     * @param selectClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getClientDetail(SelectClientDTO selectClientDTO){
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        selectClientDTO.setUserId(sessionUserVO.getId());
        selectClientDTO.setRoleCode(sessionUserVO.getSessionRoleVo().get(0).getRoleCode());
        return customerService.getCustomerDetail(selectClientDTO);
    }

    /**
     * @author: 张霞
     * @description：客户编辑信息
     * @createDate: 11:26 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "edit",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result editClient(UpdateClientDTO updateClientDTO){
        log.info("客户编辑信息controller入参参数updateClientDTO={}",JSON.toJSONString(updateClientDTO));
        if (Objects.isNull(updateClientDTO.getManagerId())){
            Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage("客户经理不能为空");
            return result;
        }
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        return customerService.editClient(updateClientDTO);
    }

    /**
     * @author: 张霞
     * @description：客户重置密码
     * @createDate: 14:53 2018/1/22
     * @param updateClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "reset",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result resetPWD(UpdateClientDTO updateClientDTO){
        return customerService.resetPWD(updateClientDTO);
    }


    /**
     * @author: 张霞
     * @description：获取管理员所有人员
     * @createDate: 21:11 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "listUser",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getUserList(){
        User user = new User();
        user.setIsable(UserStatusEnum.NORMAL_ENUM.getCode());
        return userService.getUserByList(user);
    }

    /**
     * @author: 张霞
     * @description：获取所有客户经理数据
     * @createDate: 21:30 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "listManager",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getAllManager(){
        return managerService.getAllManager();
    }

    /**
     * @author: 张霞
     * @description：跳转到客户策略管理页面
     * @createDate: 17:12 2018/1/23
     * @param selectClientDTO
     * @param modelAndView
     * @return: java.lang.Object
     * @version: 2.1
     */
    @RequestMapping(value = "toClientListstrategysPage",method = {RequestMethod.POST,RequestMethod.GET})
    public Object toListstrategysPage(SelectClientDTO selectClientDTO, ModelAndView modelAndView){

        modelAndView.setViewName("user/strategy_client");
        modelAndView.getModel().put("userId",selectClientDTO.getId());
        return modelAndView;
    }
}
