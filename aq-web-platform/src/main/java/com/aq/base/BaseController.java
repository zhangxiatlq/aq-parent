package com.aq.base;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.config.shiro.MyShiroRealm;
import com.aq.core.constant.CommonConstants;
import com.aq.facade.dto.permission.UserDTO;
import com.aq.facade.service.permission.ILoginService;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.facade.vo.permission.UserVO;
import com.aq.util.result.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;


/**
 * 用于获取用户信息等公共操作
 *
 * @author 郑朋
 * @create 2018/1/22
 */
public class BaseController {

    @Autowired
    MyShiroRealm myShiroRealm;

    @Reference(version = "1.0.0")
    ILoginService loginService;

    //用户接口
    @Reference(version = "1.0.0")
    private IUserService userService;


    /**
     * 通过用户员工号踢出用户Session
     *
     * @param employeeID
     * @return void
     * @author 郑朋
     * @create 2018/1/22
     */
    public void forceLogoutUsersByUserId(String employeeID) {

        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        if (sessions != null && sessions.size() != 0) {
            for (Session session : sessions) {
                //清除该用户以前登录时保存的session
                if (employeeID.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionManager.getSessionDAO().delete(session);
                }
            }
        }
    }


    /**
     * 获取登录人的session
     *
     * @param
     * @return com.aq.facade.vo.permission.SessionUserVO
     * @author 郑朋
     * @create 2018/1/22
     */
    public SessionUserVO getLoginUser() {
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        SessionUserVO info = (SessionUserVO) currentUser.getSession().getAttribute(CommonConstants.LOGIN_BACK_USER_SESSION);
        //如果是记住我，处理Session失效
        if (info == null) {
            Object employeeID = currentUser.getPrincipal();
            if (employeeID != null) {
                boolean isRemembered = currentUser.isRemembered();
                if (isRemembered) {
                    //清除权限缓存
                    myShiroRealm.getAuthorizationCache().remove(currentUser.getPrincipals());
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmployeeID(employeeID.toString());
                    List<UserVO> list = (List<UserVO>) userService.getUserByCondition(userDTO).getData();
                    if (CollectionUtils.isNotEmpty(list)) {
                        UserVO userVO = list.get(0);
                        //对密码进行加密后验证
                        UsernamePasswordToken token = new UsernamePasswordToken(userVO.getEmployeeID(), userVO.getPassword(),
                                currentUser.isRemembered());
                        //把当前用户放入session
                        currentUser.login(token);
                        Session session = currentUser.getSession();
                        UserDTO user = new UserDTO();
                        user.setEmployeeID(userVO.getEmployeeID());
                        user.setPassword(userVO.getPassword());
                        Result result = loginService.login(user);
                        session.setAttribute(CommonConstants.LOGIN_BACK_USER_SESSION, result.getData());
                        info = (SessionUserVO) result.getData();
                    }
                }
            }

        }
        return info;

    }
}
