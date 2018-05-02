package com.aq.config.shiro;

import com.aq.core.constant.CommonConstants;
import com.aq.facade.dto.permission.UserDTO;
import com.aq.facade.service.permission.IBtnService;
import com.aq.facade.service.permission.IMenuService;
import com.aq.facade.service.permission.IRoleService;
import com.aq.facade.service.permission.IUserService;
import com.aq.facade.vo.permission.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Shiro
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {


    //用户接口
    @Autowired
    private IUserService userService;
    //角色接口
    @Autowired
    private IRoleService roleService;
    //菜单接口（后面要改成权限接口）
    @Autowired
    private IMenuService menuService;
    //按钮接口
    @Autowired
    private IBtnService btnService;

    /**
     * 获取菜单接口方法，用于配置文件调用菜单接口（后面要改为获取权限接口）
     *
     * @return 菜单接口（后面要改为权限接口）
     * @author zhengpeng
     * @createTime 2017年05月10日
     */
    public IMenuService getMenuService() {
        return menuService;
    }

    /**
     * 获取按钮接口方法，用于配置文件调用菜单接口（后面要改为获取权限接口）
     *
     * @return 按钮接口（后面要改为权限接口）
     * @author zhengpeng
     * @createTime 2017年05月13日
     */
    public IBtnService getBtnService() {
        return btnService;
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * 经测试：本例中该方法的调用时机为需授权资源被访问时
     * 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     *
     * @param principalCollection
     * @return AuthorizationInfo
     * @author zhengpeng
     * @createTime 2017/5/15 13:56
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("##################执行Shiro权限认证##################");

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        SessionUserVO sessionUserVO = (SessionUserVO) currentUser.getSession().getAttribute(CommonConstants.LOGIN_BACK_USER_SESSION);

        Byte roleCode = sessionUserVO.getSessionRoleVo().get(0).getRoleCode();
        String loginName = (String) super.getAvailablePrincipal(principalCollection);
        //2.查询用户是否存在
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeID(loginName);
        userDTO.setRoleCode(roleCode);
        List<UserVO> userVOList = (List<UserVO>) userService.getUserByCondition(userDTO).getData();
        //3.如果用户存在，获取该用户的角色集合和权限集合
        if (userVOList != null && userVOList.size() > 0) {
            //权限信息对象info,用来存放用户的所有角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //用户的角色集合
            List<RoleVO> roles = roleService.getRoleByUserId(userVOList.get(0).getId());
            info.setRoles(ConvertRoleListUtil.ConvertRoleListToSet(roles));

            //菜单权限
            List<Integer> roleList = new ArrayList<>();
            for (RoleVO role : roles) {
                roleList.add(role.getId());
            }
            List<MenuVO> menuVOS = menuService.getMenuByRoleIds(roleList);
            info.addStringPermissions(ConvertRoleListUtil.ConvertPermissionListToStringList(menuVOS));

            //按钮权限
            //Session按钮集合
            Collection<String> permissions = new HashSet<>();
            List<Integer> menuList = new ArrayList<>();
            for (MenuVO m : menuVOS) {
                menuList.add(m.getId());
            }
            for (Integer roleId : roleList) {
                List<BtnVO> btnVOS = btnService.getBtnListByMenuId(menuList, roleId);
                info.addStringPermissions(ConvertRoleListUtil.ConvertBtnPermissionListToStringList(btnVOS));
                permissions.addAll(ConvertRoleListUtil.ConvertBtnPermissionListToStringList(btnVOS));
            }
            log.info("该用户总角色数为：{}", info.getRoles().size());
            log.info("角色详细列表如下:");
            for (String s : info.getRoles()) {
                log.info(s);
            }
            log.info("该用户总权限数为：{}", info.getStringPermissions().size());
            log.info("权限详细列表如下:");
            for (String s : info.getStringPermissions()) {
                log.info(s);
            }

            //获取当前的Subject把权限按钮放入Session中
            Session session = currentUser.getSession();
            for (String s : permissions) {
                session.setAttribute(s, s);
            }
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }


    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @author 郑朋
     * @create 2018/1/20
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        log.info("验证当前Subject时获取到token为：{}", ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        //1.查询用户是否存在
        String username = token.getUsername();
        String employeeID = username.split("-")[0];
        String roleCode = username.split("-")[1];
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeID(employeeID);
        userDTO.setRoleCode(Byte.valueOf(roleCode));
        List<UserVO> userVOList = (List<UserVO>) userService.getUserByCondition(userDTO).getData();
        if (userVOList == null || userVOList.size() == 0) {
            throw new UnknownAccountException();
        } else {
            UserVO user = userVOList.get(0);
            if (user.getIsable() == 0) {
                throw new LockedAccountException();
            } else {
                // 若账户存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
                return new SimpleAuthenticationInfo(user.getEmployeeID(), user.getPassword(), getName());
            }
        }
    }


    public static void main(String[] args) {

        //List<UserVO> userVOList = (List<UserVO>) [];
    }

}