package com.aq.controller.permission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.base.BaseController;
import com.aq.config.shiro.MyShiroRealm;
import com.aq.core.constant.CommonConstants;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.permission.UserDTO;
import com.aq.facade.exception.permission.PermissionExceptionEnum;
import com.aq.facade.service.permission.ILoginService;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.permission.SessionRoleVO;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.encrypt.MD5;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.verify.VerifycodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 *
 * @author 郑朋
 * @create 2018/1/22
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Reference(version = "1.0.0")
    ILoginService loginService;

    @Autowired
    MyShiroRealm myShiroRealm;

    @Reference(version = "1.0.0")
    IUserService userService;

    /**
     * 跳转登录页面
     *
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginPage() {
        return "redirect:login";
    }


    /**
     * 跳转首页
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        //返回index需要的用户名和角色信息
        SessionUserVO su = getLoginUser();
        if (null != su) {
            mv.addObject("userId", su.getId());
            mv.addObject("userName", su.getUserName());
            StringBuffer sb = new StringBuffer();
            if (su.getSessionRoleVo() != null && su.getSessionRoleVo().size() > 0) {
                for (SessionRoleVO sr : su.getSessionRoleVo()) {
                    sb.append(sr.getRoleName()).append(",");
                }
                String str = sb.toString();
                String roleNames = str.substring(0, str.length() - 1);
                mv.addObject("roleName", roleNames);
            } else {
                mv.setViewName("login");
                mv.addObject("code", PermissionExceptionEnum.NO_ROLE.getCode());
                mv.addObject("msg", PermissionExceptionEnum.NO_ROLE.getMsg());
            }
        }
        return mv;
    }


    /**
     * 跳转登录
     *
     * @return java.lang.Object
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login() {
        return "login";
    }


    /**
     * 加载验证码
     *
     * @param request
     * @param response
     * @return
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/loadverify", method = RequestMethod.GET)
    public void loadVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        VerifycodeUtils.makeVerifyImageByNum(request, response, 4);
    }


    /**
     * 校验验证码
     *
     * @param request
     * @param msgCode
     * @return
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/checkVerify", method = RequestMethod.GET)
    @ResponseBody
    public Result checkVerify(HttpServletRequest request, String msgCode) {
        //1.图片验证码验证
        String verifyCode = msgCode;
        String code = VerifycodeUtils.getVerifyCodeBySession(request);
        Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
        if (StringUtils.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(code)) {
            result = ResultUtil.getResult(RespCode.Code.FAIL);
            result.setMessage("验证码错误");
        }
        return result;
    }


    /**
     * 注销登录
     *
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            log.error("注销登录处理错误", e);
        }
        return "redirect:login";
    }


    /**
     * 重定向到没有权限返回页面
     *
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping("/403")
    public String unauthorizedRole() {
        log.info("------没有权限-------");
        return "redirect:noPermission";
    }


    /**
     * 没有权限返回页面
     *
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping("/noPermission")
    public String noPermission() {
        return "noPermission";
    }


    /**
     * 欢迎页
     *
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping("/welcome")
    public String toWelcome() {
        return "welcome";
    }


    /**
     * 交给shiro管理登录
     *
     * @param user
     * @param rememberMe
     * @param msgCode
     * @param request
     * @return java.lang.Object
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@ModelAttribute UserDTO user, boolean rememberMe, String msgCode, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        //1.图片验证码验证
        String verifyCode = msgCode;
        String secode = VerifycodeUtils.getVerifyCodeBySession(request);
        if (verifyCode == null || !verifyCode.equalsIgnoreCase(secode)) {
            mv.setViewName("login");
            mv.addObject("code", 1111);
            mv.addObject("msg", "验证码错误");
            return mv;
        }
        //2.登录认证 默认管理员
        user.setRoleCode(RoleCodeEnum.ADMIN.getCode());
        String username = user.getEmployeeID();
        username = username + "-" + user.getRoleCode();
        //MD5加密
        user.setPassword(MD5.getMD5Str(user.getPassword()));
        UsernamePasswordToken token = new UsernamePasswordToken(username, user.getPassword(), rememberMe);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.info("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            log.info("对用户[" + username + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            mv.setViewName("login");
            if (uae.getMessage() != null && !"".equals(uae.getMessage().trim())) {
                mv.addObject("msg", uae.getMessage());
            } else {
                mv.addObject("msg", PermissionExceptionEnum.UNKNOWN_ACCOUNT.getMsg());
            }
            mv.addObject("code", PermissionExceptionEnum.UNKNOWN_ACCOUNT.getCode());
            return mv;
        } catch (IncorrectCredentialsException ice) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            mv.setViewName("login");
            mv.addObject("code", PermissionExceptionEnum.INCORRECT_CREDENTIALS.getCode());
            mv.addObject("msg", PermissionExceptionEnum.INCORRECT_CREDENTIALS.getMsg());
            return mv;
        } catch (LockedAccountException lae) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            mv.setViewName("login");
            mv.addObject("code", PermissionExceptionEnum.LOCKED_ACCOUNT.getCode());
            mv.addObject("msg", PermissionExceptionEnum.LOCKED_ACCOUNT.getMsg());
            return mv;
        } catch (ExcessiveAttemptsException eae) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            mv.setViewName("login");
            mv.addObject("code", PermissionExceptionEnum.EXCESSIVE_ATTEMPTS.getCode());
            mv.addObject("msg", PermissionExceptionEnum.EXCESSIVE_ATTEMPTS.getMsg());
            return mv;
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            log.error("堆栈轨迹，ae={}", ae);
            mv.setViewName("login");
            mv.addObject("code", PermissionExceptionEnum.AUTHENTICATION.getCode());
            mv.addObject("msg", PermissionExceptionEnum.AUTHENTICATION.getMsg());
            return mv;
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            log.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            //清除权限缓存
            myShiroRealm.getAuthorizationCache().remove(currentUser.getPrincipals());
            Session session = currentUser.getSession();
            Result result = loginService.login(user);
            session.setAttribute(CommonConstants.LOGIN_BACK_USER_SESSION, result.getData());
            mv.setViewName("redirect:index");
        } else {
            token.clear();
            mv.setViewName("redirect:login");
        }
        return mv;
    }

}
