package com.aq.controller.user;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.TransCodeEnum;
import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.dto.account.UpdateBalanceDTO;
import com.aq.facade.dto.permission.*;
import com.aq.facade.entity.permission.User;
import com.aq.facade.enums.permission.PermissionConstants;
import com.aq.facade.enums.permission.PermissionEnum;
import com.aq.facade.exception.permission.PermissionExceptionEnum;
import com.aq.facade.service.account.IAccountFlowerService;
import com.aq.facade.service.account.IBalanceService;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.account.AccountFlowerVO;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.encrypt.MD5;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理员
 *
 * @author 郑朋
 * @create 2018/1/22
 */
@Slf4j
@Controller
@RequestMapping(value = "web/user/admin")
public class UserController extends BaseController {

    @Reference(version = "1.0.0")
    IUserService userService;

    @Reference(version = "1.0.0")
    IAccountFlowerService accountFlowerService;

    @Reference(version = "1.0.0")
    IBalanceService balanceService;


    /**
     * 管理员列表
     *
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String toListRolePage() {
        return "user/admin";
    }


    /**
     * 跳转新增页面
     *
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String toAddRolePage() {
        return "user/admin/add";
    }


    /**
     * 跳转修改页面
     *
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/updatePage", method = RequestMethod.GET)
    public ModelAndView toUpdateRolePage(@ModelAttribute User user) {
        ModelAndView mv = new ModelAndView("/user/admin/update");
        Result result = userService.getUserById(user);
        mv.addObject("user", result.getData());
        return mv;
    }


    /**
     * 获取用户拥有的角色列表
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/getUserRole", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserRole(Integer userId) {
        return userService.getUserRoleByUserId(userId);
    }


    /**
     * 分配角色
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/updateUserPermission", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserRole(@ModelAttribute UpdateUserRoleDTO ud) {
        //判断角色是否选择
        if (CollectionUtils.isEmpty(ud.getRoleIds())) {
            return ResultUtil.getResult(PermissionExceptionEnum.ROLE_NOT_NULL_ERROR);
        }
        SessionUserVO sessionUserVO = getLoginUser();
        ud.setCreaterId(sessionUserVO.getId());
        ud.setUpdaterId(sessionUserVO.getId());
        Result result = userService.updateUserRole(ud);
        //踢出该用户
        if (result.isSuccess()) {
            forceLogoutUsersByUserId(ud.getEmployeeID());
        }
        return result;
    }


    /**
     * 分页获取列表
     *
     * @param page
     * @param selectUserDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserListByPage(@ModelAttribute PageBean page, @ModelAttribute SelectUserDTO selectUserDTO) {
        return userService.getUserListByPage(page, selectUserDTO);
    }


    /**
     * 新增用户
     *
     * @param addUserDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addUser(@ModelAttribute AddUserDTO addUserDTO) {
        SessionUserVO sessionUserVO = getLoginUser();
        addUserDTO.setPassword(MD5.getMD5Str(PermissionConstants.PASSWORD));
        addUserDTO.setCreaterId(sessionUserVO.getId());
        return userService.addUser(addUserDTO);
    }


    /**
     * 修改用户
     *
     * @param user
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@ModelAttribute User user) {
        SessionUserVO sessionUserVO = getLoginUser();
        user.setUpdaterId(sessionUserVO.getId());
        return userService.updateUser(user);
    }


    /**
     * 冻结账户
     *
     * @param userStartOrFrozenDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/frozen", method = RequestMethod.POST)
    @ResponseBody
    public Result frozenCount(@ModelAttribute UserStartOrFrozenDTO userStartOrFrozenDTO) {
        SessionUserVO sessionUserVO = getLoginUser();
        userStartOrFrozenDTO.setUpdaterId(sessionUserVO.getId());
        userStartOrFrozenDTO.setIsable(PermissionEnum.NO_USE.getCode());
        Result result = userService.frozenOrEnableUser(userStartOrFrozenDTO);
        if (result.isSuccess()) {
            forceLogoutUsersByUserId(result.getData(User.class).getEmployeeID());
        }
        return result;
    }


    /**
     * 启用账户
     *
     * @param userStartOrFrozenDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result startCount(@ModelAttribute UserStartOrFrozenDTO userStartOrFrozenDTO) {
        SessionUserVO sessionUserVO = getLoginUser();
        userStartOrFrozenDTO.setUpdaterId(sessionUserVO.getId());
        userStartOrFrozenDTO.setIsable(PermissionEnum.YES_USE.getCode());
        return userService.frozenOrEnableUser(userStartOrFrozenDTO);
    }


    /**
     * 重置密码
     *
     * @param userId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    @ResponseBody
    public Result resetPass(Integer userId) {
        SessionUserVO sessionUserVO = getLoginUser();
        User user = new User();
        user.setId(userId);
        user.setUpdaterId(sessionUserVO.getId());
        return userService.resetPassword(user);
    }


    /**
     * 修改密码
     *
     * @param updatePassDto
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/updatePsw", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePsw(UpdatePassDTO updatePassDto) {
        SessionUserVO sessionUserVO = getLoginUser();
        updatePassDto.setId(sessionUserVO.getId());
        return userService.updatePass(updatePassDto);
    }

    /**
     * 设置分成比例
     *
     * @param setVIPDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/21
     */
    @RequestMapping(value = "/setVIP", method = RequestMethod.POST)
    @ResponseBody
    public Result setVIP(SetVIPDTO setVIPDTO) {
        return userService.setDivideScale(setVIPDTO);
    }

    /**
     * 设置分成比例
     *
     * @param userId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/21
     */
    @RequestMapping(value = "/getUserSecurity", method = RequestMethod.GET)
    @ResponseBody
    public Result getSecurityByUserId(Integer userId) {
        return userService.getSecurityByUserId(userId);
    }

    /**
     * 修改 用户的安全设置
     *
     * @param updateUserSecurityDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/21
     */
    @RequestMapping(value = "/setSecurity", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserRole(@ModelAttribute UpdateUserSecurityDTO updateUserSecurityDTO) {
        return userService.updateSecurity(updateUserSecurityDTO);
    }

    /**
     * 账户流水
     *
     * @param accountId
     * @param userName
     * @param model
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/3/21
     */
    @RequestMapping(value = "/balanceFlow/page", method = RequestMethod.GET)
    public String page(Integer accountId, String userName, Model model) {
        Result result = balanceService.getBalance(accountId,RoleCodeEnum.ADMIN.getCode());
        model.addAttribute("userName", userName);
        model.addAttribute("accountId", accountId);
        model.addAttribute("vo", result.getData());
        return "user/userflower";
    }

    /**
     * 账户流水 列表
     *
     * @param pageBean
     * @param balanceFlowerDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/23
     */
    @RequestMapping(value = "/balanceFlow/list", method = RequestMethod.POST)
    @ResponseBody
    public Result list(PageBean pageBean, AccountFlowerDTO balanceFlowerDTO) {
        balanceFlowerDTO.setRoleType(RoleCodeEnum.ADMIN.getCode());
        balanceFlowerDTO.setAccountId(balanceFlowerDTO.getUserId());
        if (StringUtils.isNotEmpty(balanceFlowerDTO.getCreateTimeStart())) {
            balanceFlowerDTO.setCreateTimeStart(balanceFlowerDTO.getCreateTimeStart() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(balanceFlowerDTO.getCreateTimeEnd())) {
            balanceFlowerDTO.setCreateTimeEnd(balanceFlowerDTO.getCreateTimeEnd() + " 23:59:59");
        }
        Result result =  accountFlowerService.getAccountFlowerList(pageBean, balanceFlowerDTO);
        log.info("账户流水 列表返回值， result={}", JSON.toJSONString(result));;
        return result;
    }

    /**
     * 账户结算
     *
     * @param updateBalanceDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/23
     */
    @RequestMapping(value = "/balanceFlow/settle", method = RequestMethod.POST)
    @ResponseBody
    public Result settle(UpdateBalanceDTO updateBalanceDTO) {
        updateBalanceDTO.setRoleType(RoleCodeEnum.ADMIN.getCode());
        updateBalanceDTO.setTransCodeEnum(TransCodeEnum.USER_BALANCE_SETTLE);
        updateBalanceDTO.setRemark(TransCodeEnum.USER_BALANCE_SETTLE.getMsg());
        return  balanceService.modifyBalance(updateBalanceDTO);
    }
}
