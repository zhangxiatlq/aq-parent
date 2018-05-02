package com.aq.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.UserStatusEnum;
import com.aq.facade.dto.UpdateManagerDTO;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.dto.manage.WebRegisterDTO;
import com.aq.facade.entity.permission.User;
import com.aq.facade.service.IManagerService;
import com.aq.facade.service.customer.ICustomerManageService;
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
 * @描述：客户经理controller
 * @作者： 张霞
 * @创建时间： 17:06 2018/1/23
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Controller
@RequestMapping(value = "/web/manager/")
public class ManagerController extends BaseController{

    @Reference(version = "1.0.0")
    IManagerService managerService;

    @Reference(version = "1.0.0")
    IUserService userService;

    @Reference(version = "1.0.0")
    ICustomerManageService customerManageService;

    /**
     * @author: 张霞
     * @description：跳转到客户经理列表页面
     * @createDate: 17:17 2018/1/23
     * @param
     * @return: java.lang.String
     * @version: 2.1
     */
    @RequestMapping(value = "page",method = RequestMethod.GET)
    public String toListPage(){
        return "user/manager";
    }

    /**
     * @author: 张霞
     * @description：翻页获取客户经理列表
     * @createDate: 17:20 2018/1/23
     * @param pageBean
     * @param selectManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getManagerListByPage(PageBean pageBean, SelectManagerDTO selectManagerDTO){

        log.info("翻页获取客户列表controller层入参参数selectManagerDTO={}", JSON.toJSONString(selectManagerDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        selectManagerDTO.setRoleCode(sessionUserVO.getSessionRoleVo().get(0).getRoleCode());
        selectManagerDTO.setUserId(sessionUserVO.getId());
        return customerManageService.getManagerListByPage(pageBean,selectManagerDTO);
    }

    /**
     * @author: 张霞
     * @description：获取客户经理详情信息
     * @createDate: 17:21 2018/1/23
     * @param selectManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result getManagerDetail(SelectManagerDTO selectManagerDTO){
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        selectManagerDTO.setUserId(sessionUserVO.getId());
        selectManagerDTO.setRoleCode(sessionUserVO.getSessionRoleVo().get(0).getRoleCode());
        return customerManageService.getManagerDetail(selectManagerDTO);
    }

    /**
     * @author: 张霞
     * @description：客户经理编辑信息
     * @createDate: 17:22 2018/1/23
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "edit",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result editManager(UpdateManagerDTO updateManagerDTO){
        log.info("客户编辑信息controller入参参数updateManagerDTO={}",JSON.toJSONString(updateManagerDTO));

        if (Objects.isNull(updateManagerDTO.getEmployeeId())){
            Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage("维护人员不能为空");
            return result;
        }
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }else {
            updateManagerDTO.setUpdaterId(sessionUserVO.getId());
        }

        return customerManageService.editManager(updateManagerDTO);
    }

    /**
     * @author: 张霞
     * @description：客户经理重置密码
     * @createDate: 17:26 2018/1/23
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @RequestMapping(value = "reset",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result resetPWD(UpdateManagerDTO updateManagerDTO){

        return customerManageService.resetPWD(updateManagerDTO);
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
     * @description：跳转到客户经理策略管理页面
     * @createDate: 17:28 2018/1/23
     * @param selectManagerDTO
     * @param modelAndView
     * @return: java.lang.Object
     * @version: 2.1
     */
    @RequestMapping(value = "toManagerListstrategysPage",method = {RequestMethod.POST,RequestMethod.GET})
    public Object toListstrategysPage(SelectManagerDTO selectManagerDTO, ModelAndView modelAndView){

        modelAndView.setViewName("user/strategy_manager");
        modelAndView.getModel().put("userId",selectManagerDTO.getId());
        return modelAndView;
    }

    /**
     * @Creater: zhangxia
     * @description：添加客户经理
     * @methodName: addManager
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 14:12 2018-2-26
     */
	@ResponseBody
	@RequestMapping(value = "addManager", method = RequestMethod.POST)
	public Result addManager(WebRegisterDTO dto) {

		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		log.info("添加客户经理入参dto={}", JSON.toJSONString(dto));
		try {
		    SessionUserVO sessionUserVO = getLoginUser();
		    if (Objects.isNull(sessionUserVO)){
		        return result = ResultUtil.getResult(RespCode.Code.NOT_LOGIN);
            }
		    dto.setManagerDivideScale(1.0);//默认设置vip分润比为1
			dto.setCreaterId(sessionUserVO.getId());
			result = customerManageService.webRegisterManager(dto);
		} catch (Exception e) {
			log.error("添加客户经理异常e={}", e);
		}
		log.info("添加客户经理返回数据result={}", JSON.toJSONString(result));
		return result;
	}

    /**
     * @author: zhangxia
     * @desc:编辑客户经理 的分润比例
     * @params: [dto]
     * @methodName:updateDivide
     * @date: 2018/3/23 0023 上午 11:43
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @RequestMapping(value = "updateDivide",method = RequestMethod.POST)
    @ResponseBody
	public Result updateDivide(UpdateManagerDTO dto){
        log.info("编辑客户经理 的分润比例 controller 入参参数dto={}",JSON.toJSONString(dto));
        return  customerManageService.updateDivideScalse(dto);
    }
}
